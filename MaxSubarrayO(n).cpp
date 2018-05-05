#include <iostream>
#include <vector>
using namespace std;
int maxSubArray(vector<int>& nums) {
    int n = nums.size();
    if(n == 0)
        return 0;
    if(n == 1)
        return nums[0];
    vector<int> sum(n,0);
    sum[0] = nums[0];
    int max_sum = nums[0];
    for(int i=1;i<n;++i)
    {
        sum[i]=max(sum[i-1]+nums[i],nums[i]);
        max_sum=max(sum[i],max_sum);
    }
    return max_sum;
}