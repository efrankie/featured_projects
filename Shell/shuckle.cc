/*  --------Frankie Wilson--------
    ---CS426, the BIG assigment---
    --------shuckle.cc------------
*/
#include <iostream>
#include <fstream>
#include <cstdlib>
#include "readline/readline.h"
#include "readline/history.h"
#include <libexplain/execvp.h> // explain_execvp_or_die()
// https://linux.die.net/man/3/explain_execvp_or_die
// sudo apt-get install libexplain-dev
// g++ shuckle.cc -o shuckle -std=c++14 -lreadline -lexplain
// new favorite function name, right???
#include <libexplain/dup2.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <signal.h>

#include "shuckleHeader.h"
using namespace std;

// globals
string path;
string line, filename, cmd;
char *arg[15];
vector<char *> args;
int num, fd, pos;
int pathPos = 0;

int main(int argc, char *argv[]){
    start_shell();
    run_shell();
}

void run_shell(){
    using_history();
    // while get a line
    while ((line = readline(getenv("PS1"))).c_str()){
        // harvesting zombies 1.0
        int wait1 = -1;
        while(waitpid(-1, 0, WNOHANG)==0){
            wait1 = waitpid(-1, 0, WNOHANG);
        }
        // if input is a blank line ask for new input instead of giving up
        if(line == "" ||line == " "){
            continue;
        }
        char *lineNew = removeConst(line);
        string lowerline = lowerCase(line);
        if (lowerline == "quit" || lowerline == "exit"){
            return;
        }
        // split into array and get num of commands, cmds will never be more than sz 15
        num = sscanf(line.c_str(), "%ms %ms %ms %ms %ms %ms %ms %ms %ms %ms %ms %ms %ms %ms %ms",
                     &arg[0], &arg[1], &arg[2], &arg[3], &arg[4], &arg[5], &arg[6], &arg[7],
                     &arg[8], &arg[9], &arg[10], &arg[11], &arg[12], &arg[13], &arg[14]);
        // move to vector
        args.insert(args.begin(), arg, arg + num);
        args.push_back(NULL); // execvp expects NULL as last element

        // if first word is "cd"
        if (line.substr(0, 3) == "cd "){
            // chdir
            if (chdir(args[1]) != 0) 
                cerr << "chdir() failed\n";
        }

        // if conatins "="
        else if (line.find('=') != string::npos) {
            putenv(lineNew);
        }
        else { 
            // if conatins "|" pipe
            if (line.find('|') != string::npos){ 
                doPipeCommands(); 
            }
            // else doCommand   
            else {
                int pid = fork();
                checkFork(pid, "regular");
                if (pid == 0){ // if kid
                    doCommand(args);
                }
                else {// got a parent, gross
                    waitpid(pid, 0, 0); // wait for a kid
                }
            }
        }
        // harvesting zombies 2.0
        int wait2 = -1;
        while(waitpid(-1, 0, WNOHANG)==0){
            wait2 = waitpid(-1, 0, WNOHANG);
        }
        // adding readline controls
        cout << "[" << line << "]" << endl;
        if (*line.c_str()) add_history(line.c_str());
        args.clear();
    }
}

void doCommand(vector<char *> cmds){
    //cout << "\033[33mHere's what you want:\033[0m \n";
    // if there is a ">" or "<" or ">>" redirect
    if (line.find('>') != string::npos || line.find('<') != string::npos){
        //cout << "made it here x7.2 redirection\n";
        int newfd = redirect(cmds);
        explain_dup2_or_die(fd, newfd);
        cmds.erase(cmds.begin() + pos);
        cmds.erase(cmds.begin() + pos);
    }
    char **arr = &cmds[0]; // converting arglist back to array so execvp doesn't have a stroke
    if (cmds[0][0]=='/'){
        // Execute using the full path given
        execvp(cmds[0], arr);
    }

    // for each item in path
    path = getenv("PATH"); // getting the path(s)
    for (int i = 0; i < path.length(); i++){
        if(path[i]==':'){
            cmd = path.substr(pathPos,i-pathPos);
            cmd = cmd + "/" + cmds[0];
            pathPos = i+1;
            // exec
            if((execvp(cmd.c_str(), arr)) != -1){ // try each path til we get the right one
                return; // got the right one, don't bother with the rest
            }
        }
    }
    // cout bad bad bad
    cerr << "Command not supported... or it was nonsense.\n";
    exit(1);
}

void doPipeCommands(){
    int pipePos;
    int counter = 0;
    vector<char *> cmd1;
    vector<char *> cmd2;
    for (int i = 0; i < num; i++){
        string s = args[i];
        if (s.find("|") != string::npos){
            pipePos = i;
            break;
        }
    }
    for (int j = 0; j < pipePos; j++){
        cmd1.push_back(args[j]);
    }
    for (int j = pipePos+1; j < args.size()-1; j++){
        cmd2.push_back(args[j]);
    }
    cmd1.push_back(NULL);
    cmd2.push_back(NULL);
    // Create a pipe. 
    int pipefd[2]; 
    int ppid = pipe(pipefd); 
    checkPipe(ppid);
    if (ppid == 0) {
        // Create our first process. 
        int pid = fork();
        checkFork(pid, "piping");
        if (pid == 0) { 
            close(pipefd[0]);   
            explain_dup2_or_die(pipefd[1], 1);
            doCommand(cmd1);
            exit(1);
        }    
        else { // parent
            close(pipefd[1]); 
            int pid2 = fork();
            checkFork(pid2, "piping 2");
            if (pid2 == 0){ // child 2.0 
                explain_dup2_or_die(pipefd[0], 0); 
                close(pipefd[0]);
                doCommand(cmd2);
                exit(1);
            }else {  // Parent process
                close(pipefd[0]);
                // Wait for both child processes to finish
                waitpid(pid, NULL, WNOHANG);
                waitpid(pid2, NULL, WNOHANG);
            }
        }
    } 
}

int redirect(vector<char *> cmds){
    for (int i = 0; i < cmds.size()-1; i++){
        string s = cmds[i];
        pos = i;
        filename = cmds[i + 1];
        // ">>" append
        if (s.find(">>") != string::npos){
            //cout << filename << endl;
            fd = open(filename.c_str(), O_RDWR | O_APPEND);
            checkFile(fd, filename);
            return 1;
        }
        // '>' redir out, creates and writes
        if (s.find('>') != string::npos){
            //cout << "filename = " << filename << endl;
            fd = open(filename.c_str(), O_RDWR | O_CREAT | O_TRUNC , 0666);
            checkFile(fd, filename);
            return 1;
        }
        // '<' redir in
        if (s.find('<') != string::npos){
            //cout << filename << endl;
            fd = open(filename.c_str(), O_RDONLY);
            checkFile(fd, filename);
            return 0;
        }
    }
    cerr << "Redirection messed up\n";
    exit(1);
}