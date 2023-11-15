/*
Frankie Wilson
426 - HW 04
Client
*/

#include "header.h"
using namespace std;

struct shmbuf *shmp;
struct timespec ts;

int main(int argc, char *argv[])
{
    //cout << " made it here to client\n";
    // Opens and maps the named region
    int fd = shm_open(argv[1], O_RDWR, 0);
    checkOpen(fd);
    shmp = (shmbuf *)mmap(NULL, sizeof(*shmp), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    checkMap(shmp, fd);

    // Wait for data to become available no more than 1 second
    // Calculate relative interval as current time
    if (clock_gettime(CLOCK_REALTIME, &ts) == -1)
    {
        perror("ERROR: clock isn't doing its job");
        exit(EXIT_FAILURE);
    }
    ts.tv_sec += 1;

    //  Wait for data to become available
    if (sem_timedwait(&shmp->sem, &ts) == -1)
    {
        cerr << "ERROR: server issues.\n";
    }
    else
    {
        int *value = new int;
        if (value == 0)
            shm_unlink(argv[1]);
        sem_getvalue(&shmp->sem, value);
        // print
        //cout << *value << endl;
        cout << shmp->buf << endl;
    }
    exit(EXIT_SUCCESS);
}