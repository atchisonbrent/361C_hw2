#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

//s is the number of iterations
double MonteCarlo(int s)
{
    int radius = 1;  //arbitrarily set radius to 1
    int totalInnerPoints = 0;
    int i;
    #pragma omp parallel for reduction(+:totalInnerPoints)
    for(i = 0; i < s; i++) {
        double x = (double) rand() / RAND_MAX;
        double y = (double) rand() / RAND_MAX;
        //in this case radius = 1 but wanted to represent actual formula 
        if(x * x + y * y < radius * radius) { 
            totalInnerPoints++;
        }
    }

    return 4.0 * totalInnerPoints / s;
}

void main()
{
    double pi;
    pi=MonteCarlo(100000000);
    printf("Value of pi is: %lf\n",pi);
}




