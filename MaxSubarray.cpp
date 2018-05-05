#include <iostream>
#include <vector>
using namespace std;
int maxCrossMid(vector<int>& nums,int begin,int mid ,int end);
int maxSubArray_help(vector<int>& nums,int begin,int end);

//最大子数组问题入口
int maxSubArray(vector<int>& nums) {
    int n=nums.size();
    if(n==0)
        return 0;
    if(n==1)
        return nums[0];
    return maxSubArray_help(nums,0,n-1);
}
//递归，三种情形
int maxSubArray_help(vector<int>& nums,int begin,int end) {
    if(begin==end)
        return nums[end];
    int mid=(begin+end)/2;
    int max_left=maxSubArray_help(nums,begin,mid);
    int max_right=maxSubArray_help(nums,mid+1,end);
    int max_cross=maxCrossMid(nums,begin,mid,end);
    return max(max(max_left,max_right),max_cross);
}
//跨越中间特殊处理
int maxCrossMid(vector<int>& nums,int begin,int mid ,int end){
    int left_max=INT32_MIN;
    int right_max=INT32_MIN;
    int sum=0;
    for(int i=mid;i>=begin;i--)
    {
        sum+=nums[i];
        if(sum>left_max)
            left_max=sum;
    }
    sum=0;
    for(int i=mid+1;i<=end;i++)
    {
        sum+=nums[i];
        if(sum>right_max)
            right_max=sum;
    }
    return left_max+right_max;
}

