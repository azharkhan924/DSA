class Demo{
	public static void main(String[] args) {

		//int nums[] = {-2,1,-3,4,-1,2,1,-5,4};

		//int nums[] = {1};
		int nums[] = {5,4,-1,7,8};


		int sum=0,maxsum=Integer.MIN_VALUE;
		int start=0,end=0;
		int maxstart=0,maxend=0;

        for(int i=0;i<nums.length;i++){

            sum+=nums[i];

            if(sum>maxsum){

            	maxsum=sum;

            	maxstart=start;
            	maxend=end;
            }

            if(sum<=0)
            {
                sum=0;

                start=i+1;
                end=i;
            }
            end++;

        }  
        System.out.println(maxstart +"   "+maxend );
	}
}