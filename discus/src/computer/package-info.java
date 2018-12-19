/**
 * 
 */
/**
 * 
问题一：
利用接口的实现一个计算器
实现一个的程序，要求至少能完成加减乘除运算
1.	设计一个Compute接口，还有一个double Compute(double a, double b)方法
2.	设计运算类，分别实现这个接口，完成相应运算
3.	设计一个类UseCompute，利用传递过来的对象调用Compute方法完成运算
4.	设计一个主类Test，进行测试。

  @author lyh
 
 
 java.math.BigDecimal 是java的高精度计算包 看了下面这篇博文的
 https://blog.csdn.net/loy_ouyang/article/details/79370068
 
 还没有写完， 实现了六种运算，全部直接使用Decimal包
 
 使用了范型，在计算前确认需要参与运算的数据类型，在过程中使用Decimal处理
 	考虑：在某些场合下需要固定数据类型
 	

可以添加的功能：
	设置小数位数
	取消范型，直接使用Decimal进行运算，输出也使用Decimal，如果只是为了计算需求，但是可以使用范型中以Decimal为类型
	正则表达式，使用栈进行操作，完成表达式的计算。
	
 
 */
package computer;