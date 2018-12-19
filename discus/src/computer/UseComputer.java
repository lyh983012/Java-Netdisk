package computer;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.BigInteger;


class UseCompute<T> {
	CPU cpu;
	UseCompute()
	{
		cpu=new CPU<T>();
	}
	
	public void start_calculation() {
		Scanner in =new Scanner(System.in);
		Scanner expr =new Scanner(System.in);
		int temp=0,model=0;
		String data="",datb="";
		while(true) {
			System.out.println("Please choose the mode:");
			System.out.println("1.mutlisteps ; 2.onestep ; -1.exit");
			try {
			temp=in.nextInt();
			}catch(Exception e) {
				System.out.println("illigal input_ mode ");
				break;
			}
			if(temp==-1) {
				break;
			}
			else if(temp==1) {
				System.out.println("Please set intitial data:");
				try {
					data=new String(in.next());
					}catch(Exception e) {
						System.out.println("illigal input_ init data ");
					}
				Double i=null;
				i = Double.valueOf(data);
				cpu.preset_data((T)i);
				while(true) {  									//多步计算模式
					System.out.println("choose the compute sign:");
					System.out.println("1.+ ; 2.- ; 3.* ; 4./  ;5.!  ;6.pow ;-1.rechoose model");
					try {
						temp=in.nextInt();
						}catch(Exception e) {
						System.out.println("illigal input _ sign");
					}
					if(temp==-1)
						break;
					try {
						switch(temp) {
							case 1:
								System.out.println("+");
								System.out.print("b= ");
								data=new String(in.next());
								cpu.plus((T)Double.valueOf(data));
								break;
							case 2:
								System.out.println("-");
								System.out.print("b= ");
								data=new String(in.next());
								cpu.minus((T)Double.valueOf(data));
								break;
							case 3:
								System.out.println("*");
								System.out.print("b= ");
								data=new String(in.next());
								cpu.time((T)Double.valueOf(data));
								break;
							case 4:
								System.out.println("/");
								System.out.print("b= ");
								data=new String(in.next());
								cpu.divide((T)Double.valueOf(data));
								break;
							case 5:
								System.out.println("!");
								cpu.factor();
								break;
							case 6:
								System.out.println("pow");
								System.out.print("b= ");
								data=new String(in.next());
								cpu.pow((T)Double.valueOf(data));
								break;
							default:
								System.out.println("illigal input _ sign");
					}}catch(Exception e) {
						System.out.println("illigal input _ number");
					}
				}
			}else if(temp==2) {						//单步计算模式
				System.out.println("choose the compute sign:");
				System.out.println("1.+ ; 2.- ; 3.* ; 4./  ;5.!  ;6.pow ;-1.rechoose model");
				try {
					temp=in.nextInt();
					}catch(Exception e) {
						System.out.println("illigal input_ mode ");
					}
				if(temp==-1)
					break;
				try {
					switch(temp) {
						case 1:
							System.out.print("input a= ");
							data=new String(in.next());
							System.out.println("+");
							System.out.print("input b= ");
							datb=new String(in.next());
							cpu.plus((T)Double.valueOf(data),(T)Double.valueOf(datb));
							break;
						case 2:
							System.out.print("input a= ");
							data=new String(in.next());
							System.out.println("-");
							System.out.print("input b= ");
							datb=new String(in.next());
							cpu.minus((T)Double.valueOf(data),(T)Double.valueOf(datb));
							break;
						case 3:
							System.out.print("input a= ");
							data=new String(in.next());
							System.out.println("*");
							System.out.print("input b= ");
							datb=new String(in.next());
							cpu.time((T)Double.valueOf(data),(T)Double.valueOf(datb));
							break;
						case 4:
							System.out.print("input a= ");
							data=new String(in.next());
							System.out.println("!");
							System.out.print("input b= ");
							datb=new String(in.next());
							cpu.divide((T)Double.valueOf(data),(T)Double.valueOf(datb));
							break;
						case 5:
							System.out.print("input a= ");
							data=new String(in.next());
							System.out.print("!");
							cpu.factor((T)Double.valueOf(data));
							break;
						case 6:
							System.out.print("input a= ");
							data=new String(in.next());
							System.out.println("^");
							System.out.print("input b= ");
							datb=new String(in.next());
							cpu.pow((T)Double.valueOf(data),(T)Double.valueOf(datb));
							break;
						default:
							System.out.println("illigal input");
				}}catch(Exception e) {
				System.out.println("illigal input_number ");
			}
		}else {
			System.out.println("illigal input_mode please retry");
		}
	}		
			
		
	
}
}

//=========================================================
//=========================================================
//=========================================================

class CPU<T> implements Compute<T>{

	/*
	 * 属性分为：
	 * 1、data 用于保存连续运算的数据，相当于计算器中的ANS
	 * 2、result 用于暂存一次不涉及data运算的
	 * 3、exprssion 用于存储表达式，可以使用正则匹配+栈来实现表达式计算
	 */
	private T result;
	private T data;
	private StringBuffer exprssion =new StringBuffer();
	/*
	  成员函数分为：
	 * 1、给内部使用的getresult以及clear_result
	 * 2、共给外部实用的clear（清除data）以及getdata和preset
	 * 3、构造方法
	 * 4、计算表达式的方法

	 */
	CPU(){
		Integer temp=0;
		this.result=(T)temp;
		this.data=(T)temp;
		System.out.println("****************");
		System.out.println("****************");
		System.out.println("Already prepared.");
		System.out.println("****************");
		System.out.println("****************");
	}
	private void getresult() {
		System.out.println("    ");
		System.out.println("******the result is*********: "+ this.result.toString());
		System.out.println("    ");
	}
	private void clear_result() {
		Integer temp=0;
		this.result=(T)temp;
	}
	public void clear() {
		Integer temp=0;
		this.result=(T)temp;
		System.out.println("Cleared.");
	}
	public void getdata() {

		System.out.println("the result is: "+ this.data.toString());
	}
	public void preset_data(T a) {
	this.data=a;
	}
	/*
	 * 实现接口的函数：
	 * 各个运算的实现机制相同，只给plus注释了
	 * 
	 * 其中，两个参数的函数会输出result
	 * 只有一个参数的函数会保存data，输出本次运算的结果。
	 * 
	 *每次多步计算之前需要先调用preset，设置data，然后调用只有一个参数的函数进行计算。
	 *每次单步计算只需要输入所需数据，会直接输出结果。
	 */
	//
	@Override
	
	public T plus(T a, T b) {
		try{
		BigDecimal T1=new BigDecimal(a.toString());	//使用高精度包进行类型转换
		BigDecimal T2=new BigDecimal(b.toString());	//使用高精度包进行类型转换
		BigDecimal result=T1.add(T2);				//使用高精度包进行计算
		this.result=(T)result;						//类型转换为所需类型
		getresult();
		}catch(Exception e) {
			System.out.println("Illegal operations.");
		}
		return this.result;
	}
	@Override
	public T plus(T b) {
		T result=plus(this.data , b);				//调用plus的重载
		this.data=this.result;						//转移临时存储的result到data里
		clear_result();
		System.out.println("Answer has been saved.");
		return this.data;
	}
	@Override
	public T divide(T a, T b) {
		try{
		BigDecimal T1=new BigDecimal(a.toString());
		BigDecimal T2=new BigDecimal(b.toString());
		BigDecimal result=T1.divide(T2);
		this.result=(T)result;
		getresult();
		}catch(Exception e) {
			System.out.println("Illegal operations.");
		}
		return this.result;
	}
	@Override
	public T divide(T b) {
		T result=divide(this.data , b);
		this.data=this.result;
		System.out.println("Answer has been saved.");
		clear_result();
		return this.data;
	}
	@Override
	public T minus(T a, T b) {
		try{
		BigDecimal T1=new BigDecimal(a.toString());
		BigDecimal T2=new BigDecimal(b.toString());
		BigDecimal result=T1.subtract(T2);
		this.result=(T)result;
		getresult();
		}catch(Exception e) {
			System.out.println("Illegal operations.");
		}
		return this.result;
	}
	@Override
	public T minus(T b) {
		T result=minus(this.data , b);
		this.data=this.result;
		System.out.println("Answer has been saved.");
		clear_result();
		return this.data;
	}
	@Override
	public T time(T a, T b) {
		try{
		BigDecimal T1=new BigDecimal(a.toString());
		BigDecimal T2=new BigDecimal(b.toString());
		BigDecimal result=T1.multiply(T2);
		this.result=(T)result;
		getresult();
		}catch(Exception e) {
			System.out.println("Illegal operations.");
		}
		return this.result;
	}
	@Override
	public T time(T b) {
		T result=time(this.data , b);
		this.data=this.result;
		System.out.println("Answer has been saved.");
		clear_result();
		return this.data;
	}
	@Override
	public T factor(T n) {
		try {
				BigDecimal n11 = new BigDecimal(n.toString());
				BigDecimal I1= new BigDecimal(1);
				BigInteger n1 =n11.toBigInteger();
				BigInteger n2 =n11.toBigInteger();
				BigInteger I=I1.toBigInteger();
				for(BigInteger i=I1.toBigInteger(); n2.compareTo(i) >0 ;i=i.add(I)) {
				n1=n1.multiply(i);
				}
				this.result=(T)n1;
				getresult();

		}catch(Exception e) {
			System.out.println("illegal operation 2");				
		}		
		return this.result;
	}
	@Override
	public T factor() {
		T result=factor(this.data);
		this.data=this.result;
		System.out.println("Answer has been saved.");
		clear_result();
		return this.data;
	}
	@Override
	public T pow(T a, T b) {
		try {
		BigDecimal T1=new BigDecimal(a.toString());	//使用高精度包进行类型转换
		BigDecimal T2=new BigDecimal(b.toString());	//使用高精度包进行类型转换

		if ( Math.abs((Double)a)<100 && Math.abs((Double)b)<100) {
			this.result=(T)new Double(Math.pow((Double)a ,(Double)b));	
		}else {
			BigDecimal result=T1.pow(T2.intValue());				//使用高精度包进行计算
			System.out.println("result= "+result);				//类型转换为所需类型
		}
		this.result=this.data;
		System.out.println("Result can not be saved.");
		}catch(Exception e) {
			System.out.println("Illegle expression");
			System.out.println(e.toString());
		}
		return this.result;
	}
	public T pow(T b) {
		this.result=pow(this.data, b);						//类型转换为所需类型
		this.data=this.result;
		System.out.println("Answer has been saved.");
		clear_result();
		return this.data;
	}

}

