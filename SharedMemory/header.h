/*
Frankie Wilson
426 - HW 04
header
*/

#include <cstdlib>
#include <iostream>
#include <string.h>
#include <unistd.h>
#include <stdio.h>
#include <fstream>
#include <sysexits.h>
#include <fcntl.h>
#include <semaphore.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/shm.h>
#include <errno.h>
using namespace std;

#define errExit(msg)        \
    do                      \
    {                       \
        perror(msg);        \
        exit(EXIT_FAILURE); \
    } while (0)

#define BUF_SIZE 900

struct shmbuf
{
    sem_t sem;
    char buf[BUF_SIZE];
};

// mapping error check
void checkMap(shmbuf *map, int fd)
{
    if (map == MAP_FAILED)
    {
        cerr << "ERROR: Mapping of " << fd << " failed:\n";
        close(fd);
        exit(EX_IOERR);
    }
}

// opening error check
void checkOpen(int fd)
{
    if (fd == -1)
    {
        cerr << "ERROR: shm_open failed. \n";
    }
}