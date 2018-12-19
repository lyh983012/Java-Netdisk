package computer;


interface Compute<T> {
	
	final double pi=3.1415926535897932;
	final double e=2.718281828;
	
	public T plus(T temp);
	public T plus(T temp, T temp2);
	
	public T divide(T temp);
	public T divide(T temp, T temp2);
	
	public T minus(T temp);
	public T minus(T temp, T temp2);
	
	public T time(T temp);
	public T time(T temp, T temp2);
	
	public T factor(T temp);//阶乘
	public T factor();//阶乘
	
	public T pow(T temp,T temp2);
	public T pow(T temp);//乘方
	

}

