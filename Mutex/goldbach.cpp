/*
Frankie Wilson

426 - HW 02
Due Sep 14th
*/

#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <cmath>

using namespace std;

mutex mtx; // Mutex for locking

// checking if a number is good prime or a bad even
bool isPrime(int num)
{
    if (num <= 1)
        return false;
    for (int i = 2; i <= num / 2; i++)
        if (num % i == 0)
            return false;

    return true;
}

// finding goldbach pairs and updating max
void findGoldbachPairs(int start, int end, int &max, int &maxPrime1, int &maxPrime2){
    for (int i = start; i <= end; i += 2){
        if (isPrime(i)){
            for (int j = i; j <= end; j += 2){
                if (isPrime(j) && i + j == end){
                    mtx.lock(); // Lock before updating max
                    if (i > maxPrime1){
                        maxPrime1 = i;
                        maxPrime2 = j;
                        max = end;
                    }
                }
            }
        }
    }
}

int main(){
    const int evenNumber = 1000; // The even number for which we want to find Goldbach pairs
    int maxPrime1 = 0, maxPrime2 = 0, max = 0;
    int numOfThreads = 4; // Number of threads to use

    vector<thread> threads;

    for (int i = 0; i < numOfThreads; ++i){
        int start = i * (evenNumber / numOfThreads);
        int end = (i + 1) * (evenNumber / numOfThreads) - 1;
        threads.emplace_back(findGoldbachPairs, start, end, ref(max), ref(maxPrime1), ref(maxPrime2));
    }

    // Wait for all threads to finish
    for (auto &thread : threads){
        thread.join();
    }

    cout << "The largest Goldbach pair with the largest minimum prime for " << evenNumber << " is: "
              << maxPrime1 << " + " << maxPrime2 << " = " << max << std::endl;

    return 0;
}