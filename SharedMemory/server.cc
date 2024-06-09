/*
Frankie Wilson
426 - HW 04
server
*/

#include "header.h"
using namespace std;

int main(int argc, char *argv[])
{

    int fd;
    char *shmpath;
    struct shmbuf *shmp;
    int ct = 0;

    for (int i = 1; i < argc; i += 3)
    {
        // Set up N shared memory regions
        shmpath = argv[i];
        fd = shm_open(shmpath, O_CREAT | O_RDWR, 0600);
        checkOpen(fd);
        // truncate to size needed
        if (ftruncate(fd, 100) == -1)
            errExit("Error: Truncate better");
            
        // map error check
        shmp = (shmbuf *)mmap(NULL, sizeof(*shmp), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
        checkMap(shmp, fd);
        // Copy the data to the region
        strcpy(shmp->buf, argv[i + 1]);

        // update the semaphore
        if (sem_init(&shmp->sem, 1, atoi((argv[i + 2]))) == -1)
            cerr << "ERROR: semaphore not updating correctly\n";
    }
    // When all regions are used ... exit
    exit(EXIT_SUCCESS);
}