package pyp.navigation.theme;

/**
 * @Title: ISkin
 * @Description: 接口(被改变皮肤的)
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public interface ISkin {
	/**
	 * 获取皮肤Key
	 * 每一个需要改变皮肤的类，都要提供一个独一无二的Key来进行区分
	 * 可以使用中文，例如：return "主页菜单栏背景";
	 * @return String
	 * 唯一的标识
	 */
	public String getSkinKey();
	
	
	/**
	 * 改变皮肤
	 * 具体实现代码具体实现
	 * @param skin_num
	 * 要更换的皮肤的编号，需要在对应的Context类中进行设置
	 * @return boolean
	 * 是否更换成功
	 */
	public boolean changeSkin(int skin_num);
}
