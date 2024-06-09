// Frankie Wilson
// 426 - HW 03, memory mapping 

#include <iostream>
#include <string.h>
#include <cstdlib>
#include <fstream>
#include <stdio.h>
#include <unistd.h>
#include <sysexits.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
using namespace std;

void checkOpen(char *file, int fd);
void checkMap(char *mapfd, int fd);
int main(int argc, char *argv[]);

long posTracker = 0;

// file open error check
void checkOpen(char *file, int fd)
{
    if (fd == -1)
    {
        cerr << "ERROR: File " << file << " couldn't be opened:\n";
        cerr << "%d - %s\n", errno, strerror(errno);
        exit(EX_NOINPUT);
    }
    cout << file << endl;
}

// file mapping error check
void checkMap(char *mapfd, int fd)
{
    if (mapfd == MAP_FAILED)
    {
        cerr << "ERROR: Mapping of " << fd << " failed:\n";
        cerr << "%d - %s\n", errno, strerror(errno);
        close(fd);
        exit(EX_IOERR);
    }
}

int main(int argc, char *argv[])
{
    long size = 0;
    for (int i = 1; i < argc - 1; i++)
    {
        // open ALL the files
        int fd = open(argv[i], O_RDONLY);
        // & error check
        checkOpen(argv[i], fd);

        // getting the size of all the files (not including the destination)
        long fileSize = 0;
        fileSize = lseek(fd, 0, SEEK_END);
        size = size + fileSize;
        cout << "size = " << size << endl;

        // mmap files & error check
        char *map_fd = (char *)(mmap(NULL, size, PROT_READ, MAP_PRIVATE, fd, 0));
        checkMap(map_fd, fd);
        madvise(map_fd, size, MADV_SEQUENTIAL);

        // destination, open & error check
        int dest_fd = open(argv[argc - 1], O_RDWR | O_CREAT | O_TRUNC, 0644);
        checkOpen(argv[argc - 1], dest_fd);
        cout << dest_fd << endl;

        // checking current output file size/info
        // cout << fstat(dest_fd, &info) << endl;

        // set destination file size by truncating larger as needed
        ftruncate(dest_fd, size);

        // checking new output file size/info
        // fstat(dest_fd, &info);
        // cout << fstat(dest_fd, &info) << endl;

        // mmap dest & error check & advise
        char *dest_map = (char *)(mmap(NULL, size, PROT_WRITE, MAP_SHARED, dest_fd, 0));
        // cout << dest_map;
        checkMap(dest_map, dest_fd);
        madvise(dest_map, size, MADV_SEQUENTIAL);

        // copy file contents
        memcpy((dest_map + size), map_fd, size);
        posTracker += fileSize;

        // unmap them all
        munmap(map_fd, size);

        // and close
        close(fd);

        // close and unmap the destination file
        munmap(dest_map, size);
        close(dest_fd);
    }
}