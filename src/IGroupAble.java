
public interface IGroupAble {
	
	/**
	 * 获取要分组的值
	 * @return
	 */
	public Object getGroupByValue();
	
	
	/**
	 * 获取要聚合的字段的值
	 * @return
	 */
	public Object getConvergeFieldValue();
}
