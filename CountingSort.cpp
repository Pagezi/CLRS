//
// Created by Pagezi on 2018/5/12.
//
#include <iostream>
#include <vector>
using namespace std;

vector<int> CountSort(vector<int> A,int k)
{
    int n=A.size();
    vector<int> B(n,-1);
    vector<int> C(k,0);
    for(int i=0;i<n;i++)
        C[A[i]]++;
    for(int i=1;i<k;i++)
        C[i]=C[i]+C[i-1];
    for(int i=n-1;i>=0;i--)
    {
        B[C[A[i]]-1]=A[i];//B下标从0开始记
        C[A[i]]--;
    }
    return B;
}
