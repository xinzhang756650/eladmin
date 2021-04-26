package me.zhengjie.exception;

/**
 * 业务系统 RuntimeException 
* 业务异常， 需要对外抛错。
* 1. 接管异常信息
* 2. 单据信息与后台信息交互
* 3. 业务错误进行回滚
* 
* @author weilecai
*
*/
public class BException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String msg;//错误消息
	private boolean async; //是否异步
	
	public BException(){
		super();
	}
	
	public BException(String msg) {
		super(msg);
		this.async = false;
		this.msg = msg;
	}
	
	public BException(Exception e){
		super(e.getMessage());
		this.async = false;
		if(e instanceof BException){
			BException self = (BException) e;
		   this.msg = self.getMsg();
		}else{
			this.msg = e.getMessage();
		}
		
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}
	
	public static BException E(String msg){
		return new BException(msg);
	} 
}
