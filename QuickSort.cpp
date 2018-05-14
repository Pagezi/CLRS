//
// Created by Pagezi on 2018/5/14.
//
#include <iostream>
#include <vector>
using namespace std;

void exchange(vector<int> &A,int i,int j )
{
    int tmp=A[i];
    A[i]=A[j];
    A[j]=tmp;
}
int PartitionLomuto(vector<int> &A,int p,int r)
{
    int x=A[r];
    int i=p-1;
    for(int j=p;j<=r-1;j++)
    {
        if(A[j]<=x) {
            i++;
            exchange(A,i,j);
        }
    }
    exchange(A,r,i+1);
    return i+1;
}
int PartitionHoare(vector<int> &A,int p,int r)
{
    int x=A[r];
    int i=p-1;
    int j=r+1;
    while(true)
    {
        while(A[--j]<=x)
            if(j==p)
                break;
        while(A[++i]>x)
            if(j==r)
                break;
        if(i<j)
        {
            exchange(A,i,j);
        }
    }
}
void QucikSortLomuto(vector<int> &A,int p,int r)
{
    if(p<r)
    {
        int q=PartitionLomuto(A,p,r);
        QucikSortLomuto(A,p,q-1);
        QucikSortLomuto(A,q+1,r);
    }
}
//三向切分的快速排序
void QucikSortDijkstra(vector<int> &A,int p,int r)
{
    if(p<r)
    {
        int x=A[p];
        int lt=p;
        int gt=r;
        int i=p+1;
        while(i<=gt)
        {
            if(A[i]<x)
                exchange(A,i++,lt++);
            else if(A[i]>x)
                exchange(A,gt--,i);
            else
                i++;
        }
        QucikSortDijkstra(A,p,lt-1);
        QucikSortDijkstra(A,gt+1,r);
    }
}
void QucikSortHoare(vector<int> &A,int p,int r)
{
    if(p<r)
    {
        int q=PartitionLomuto(A,p,r);
        QucikSortHoare(A,p,q-1);
        QucikSortHoare(A,q+1,r);
    }
}