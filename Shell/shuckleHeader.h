/*  Frankie Wilson & Rose Schopfer 
    ---CS426, the BIG assigment---
     shuckleHeader for shuckle.cc 
*/
#include <iostream>
#include <string>
#include <string.h>
#include <ctype.h>
#include <vector>
using namespace std;

// declaring functions
// -lives in shuckle.cc
int main(int argc, char *argv[]);
void run_shell();
void doCommand(vector<char *> args);
void doPipeCommands();
int redirect(vector<char *> cmds);
// -lives here
void printShuckle();
void start_shell();
bool containsChar(string s, char c);
char* removeConst (string s);
string lowerCase(string s);
void checkFile(int fd, string filename);
void checkFork(int pid, string type);
void checkPipe(int pid);

char *username;

// ASCii art grabbed from: https://www.asciiart.eu/image-to-ascii
// and image sourced from: https://www.pokemon.com/uk/pokedex/shuckle
void printShuckle(){
    printf("                                ▓▒▒▒▒▒▒▓█\n");
    printf("                              █▒▒▒▒▒▒▒▒▒▒▒█\n");
    printf("                             █▒▒▒▒▒▒▒█▒▒▒▓▒█\n");
    printf("                             ▒▒▒▒▒▒▒▒█▒▒▒▓▒▓\n");
    printf("                             ▒▒▒▒▒▒▒▒▒▒▒▒▒▒█\n");
    printf("                             █▒▒▒▒▒▒▒▒▒▒▒▓█\n");
    printf("                              ▓▒▒▒▒▒▒▓██\n");
    printf("                              █▒▒▒▒▒▒█\n");
    printf("                                ▒▒▒▒▒█\n");
    printf("                                █▒▒▒▒▓\n");
    printf("                                 ▒▒▒▒█\n");
    printf("                        ███▓▓▓▓▓██▒▒▒▒█\n");
    printf("                   █▒██░░▒▓▒▒▒░░░▒▒▒▒▒█▒▒█\n");
    printf("                  ▒▓██▓▒▒▒▒▓▒░░░▒▒▒▒▒▒▓█▓▒▓\n");
    printf("                 ▓▒█▓▒▒▒▒▒█▓▒░░░▒▒▒▒▒█▒███▒█\n");
    printf("                █▒▒▒▒▒▒▒▒█▓▓▓▒░░▒▒▓██▒▒███▓█\n");
    printf("               █▓▓▓███▓▓▓▓▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒█▓▒██\n");
    printf("              █░░▒▓▓▓▓▒░▓▓▓▓▓██▓▓▓▒▒▓██▒▓▓▒▒▒▓▓█\n");
    printf("              █▒░░▒▓▓▓▓█▓████████████████▒▒▒███\n");
    printf("   ▓▒▒▒█████▒▒▓▓▒▒▒██████▓▒▒▒▒▒▒▓███████▓▒▒▓▓██\n");
    printf("  █▒▒▒▒▒▒▒▒▒▒▒▒▒▓▒▒▓████▒▒▒▒▒▒▒▒▒▒██████▒▒▒█▓▓▓▒▓▓▓▒▒▒▒▒█\n");
    printf("   ▓▒▒▒▒▒▒▒▒▒▒██▓▒▒█████▒▒▒▒▓▓█▒▒▒▒█████▒▒▒▒█▓▓▒▒▒▒▒▒▒▒▒▒█\n");
    printf("    ▓▓▓▓▓▓▒▓▓    ███████▒▓▒▒▒▒██▒▒███████▓▓   █▓▓▒▒▒▒▒▒▒▓\n");
    printf("                ████▓▓▓▓▒▒▒▒▒▒▓▓▓███████          █▓▓██\n");
    printf("             █▒▒▒▒▒▒▒▒▒▒▒▒▒▓█████\n");
    printf("             ▓▒▒▒▒▒▒▒▒▒▒▒██\n");
    printf("             █▓▒▒▒▒▒▓▓█");
}

// welcome message + important ascii art
void start_shell(){
    printShuckle();
    username = getenv("USER");
    cout <<"\n\n\033[1;31m@"<<username<<"\033[0m\033[1;33m, welcome to SHUCKLE!\033[0m\n";
    cout << "\033[33mShuckle\033[0m is a Pokémon that resembles a small turtle.\n"; 
    cout << "Its body is yellow and appears soft. Until they are in use,\n";
    cout << "Shuckle's limbs appear limp. It is encased in a very hard red shell\n";
    cout << "that has many holes in it. The holes in its shell are rimmed with white.\n\n";
    cout << "type 'exit' 'quit' or 'ctrl c' to exit shuckle\n";
    cout << "please don't use multiple pipes :(\n\n";

    string user = username;
    string prompt = "PS1=\033[31m@" + user + "\033[0m\033[33m, what do you want?\033[0m ";
    char *modPrompt = removeConst(prompt);
    putenv(modPrompt);
}

bool containsChar(string s, char c){
    for(int i = 0; i < s.length()-1; i++){
        if(s[i] == c) return true;
    }
    return false;
}

char* removeConst (string s){
    int size = s.size();
    char* sNew = new char[size];
    for (int i = 0; i < size+1; i++){
        sNew[i] = s[i];
    }
    return sNew;
}

string lowerCase(string s){

    int length = s.size();
    char s_up[length]; 
    for (int i = 0; i < length; i++){
        s_up[i] = tolower(s[i]); 
    }
    return s;
}

void checkFile(int fd, string filename){
    if (fd == -1){
        cerr << "ERROR: Cannot open " << filename << ".\n";
        exit(1);
    }
}

void checkFork(int pid, string type){
    if (pid < 0){
        cerr << "ERROR: "<< type << " forking failed.\n";
        exit(1);
    } 
}

void checkPipe(int pid){
    if (pid == -1){
        cerr << "ERROR: Piping failed.\n";
        exit(1);
    }
}