import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MemoryGroupUtils {

	/**
	 * 每个小的集合分组后的结果
	 * 
	 * @param lists
	 * @param field
	 * @return
	 */

	/**
	 * 
	 * @param lists
	 *            每个小的集合
	 * @param field
	 *            带分组的属性
	 * @return Map {"分组属性" ：{"分组属性",同一属性集合,"分组属性"}}
	 */
	private static Map<String, SubGroup<String, List, String>> _groupBy(List<? extends Map<String, Object>> lists,
			boolean isSimple,
			String... field) {
		Map<String, SubGroup<String, List, String>> rltMap = new HashMap<String, SubGroup<String, List, String>>();
		for (Map<String, Object> map : lists) {
			StringBuilder groupByKey = new StringBuilder("");
			for (String s : field) {
				groupByKey.append(map.get(s));
			}
			SubGroup<String, List, String> threed = rltMap.get(groupByKey.toString());
			if(!isSimple){
				if (threed != null) {// 之前已经有重复的值了，直接放进去即可
//					threed.getS().add(map);
					threed.getGroup().add(map);
					continue;
				}
			}
			// 之前没有重复的值，new一个出来
			List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
			list.add(map);
			SubGroup<String, List, String> three = null;
			if(isSimple){
				three = new PairGroup <String, List, String>(groupByKey.toString(), null,
						groupByKey.toString());
			}else{
				three = new ThreeGroup<String, List, String>(groupByKey.toString(), list,
						groupByKey.toString());
			}
			rltMap.put(groupByKey.toString(), three);
		}
		return rltMap;
	}

	/**
	 *  简单分组、像数据库那样进行分组 ，结果中分组的无结果集
	 */
	public static List<SubGroup> simpleGroupBy(List<Map<String, Object>> lists, final String... field) {
		return groupBy(lists, true, field);
	}
	
	/**
	 * 高级分组、像数据库那样进行分组 ，并且结果中的分组包含结果集
	 * @param lists
	 * @param field
	 * @return
	 */
	public static List<SubGroup> seniorGroupBy(List<Map<String, Object>> lists, final String... field) {
		return groupBy(lists, false, field);
	}
	
	public static List<SubGroup> groupBy(List<Map<String, Object>> lists, final boolean isSimple, final String... field) {
		// if (lists.size() > 100000) {
		List<List<Map<String, Object>>> rlList = shareByFixedNum(lists, 5);
		Master<List<Map<String, Object>>, Map<String, SubGroup<String, List, String>>> master = new Master<List<Map<String, Object>>, Map<String, SubGroup<String, List, String>>>(
				new Worker<List<Map<String, Object>>, Map<String, SubGroup<String, List, String>>>() {
					public Map<String, SubGroup<String, List, String>> handle(List<Map<String, Object>> input) {
						Map<String, SubGroup<String, List, String>> list = _groupBy(input, isSimple, field);
						return list;
					}
				}, 5);

		for (List<Map<String, Object>> list : rlList) {
			master.submitAndExe(list);
		}
		master.execute();
		// 保存最终结算结果
		Queue<Map<String, SubGroup<String, List, String>>> resultMap = master.getResultQueue();
		// 不需要等待所有Worker都执行完成，即可开始计算最终结果

		// List<Three> fianlRlt = new LinkedList<Three>();
		Map<String, SubGroup<String, List, String>> fianlRlt = new LinkedHashMap<String, SubGroup<String, List, String>>();

		while (resultMap.size() > 0 || !master.isComplete()) {
			Map<String, SubGroup<String, List, String>> col = resultMap.poll(); // 每个小的集合分组后的结果
			if (col != null) {
				Set<String> keySet = col.keySet();
				for (String string : keySet) {
					SubGroup<String, List, String> three = col.get(string);
					SubGroup<String, List, String> finalLi = fianlRlt.get(string);
					if (finalLi != null) {
						if(!isSimple){
							finalLi.getGroup().addAll(three.getGroup());
//							finalLi.getS().addAll(three.getS());
						}
						continue;
					}
					fianlRlt.put(string, three);
				}
			}
		}
		
		Set<String> finalKeySet = fianlRlt.keySet(); 
		for(String s : finalKeySet ){
			System.err.println(fianlRlt.get(s).getGroup().size());
		}
		// }
		return null;
	}

	public static List<SubGroup> groupBy(List<? extends Map<String, IGroupAble>> lists) {
		if (lists.size() > 100000) {

		}
		return null;
	}

	/**
	 * 均分一个大的集合为num个小的list如果不够分为num个返回尽量多个
	 * 
	 * @param source
	 *            源集合
	 * @param num
	 *            要分割成几个list
	 * @return
	 */
	private static <T> List<List<T>> shareByFixedNum(final List<T> source, final int num) {
		int sourceSize = source.size();
		int number = sourceSize / num;
		int remaider = sourceSize % num;
		int offset = 0;// 偏移量
		List<List<T>> rltList = new ArrayList<List<T>>(num);
		for (int i = 0; i < num; i++) {
			if (remaider > 0) {
				rltList.add(source.subList(i * number + offset, (i + 1) * number + offset + 1));
				remaider--;
				offset++;
			} else {
				rltList.add(source.subList(i * number + offset, (i + 1) * number + offset));
			}
		}
		return rltList;
	}

	/**
	 * 平分一个 source为若干个 list,其中每个list中的数据量大小为 listSize 个
	 * 
	 * @param source
	 *            源集合
	 * @param listSize
	 *            分割的集合容量
	 * @return
	 */
	@SuppressWarnings("unused")
	private static <T> List<List<T>> shareByFixedSize(final List<T> source, final int listSize) {
		List<List<T>> rltList = null;
		int sourceSize = source.size();
		if (sourceSize <= listSize) {
			rltList = new ArrayList<List<T>>(1);
			rltList.add(source);
			return rltList;
		}

		int remaider = sourceSize % listSize; // 余数
		int number = sourceSize / listSize; // 商数
		rltList = new ArrayList<List<T>>(remaider + 1);
		for (int i = 0; i < number; i++) {
			int toIndex;
			if ((i + 1) == number) {
				if (remaider == 0) {
					toIndex = ((i + 1) * listSize);
				} else {
					toIndex = ((i + 1) * listSize);
				}
			} else {
				toIndex = (i + 1) * listSize;
			}
			rltList.add(source.subList(i * listSize, toIndex));
		}
		if (remaider > 0) {
			rltList.add(source.subList(number * listSize, (number * listSize) + remaider));
		}
		return rltList;
	}

	// public static void main(String[] args) {
	// List<Integer> list = new LinkedList<Integer>();
	// Long rlt = 0L;
	// for (int i = 1; i < 10001; i++) {
	// list.add(i);
	// }
	// List<List<Integer>> li = shareByFixedSize(list, 101);
	// for (List<Integer> list2 : li) {
	// System.err.println(list2);
	// }
	// System.err.println("------------------------");
	// li = shareByFixedNum(list, 10);
	// for (List<Integer> list2 : li) {
	// System.err.println(list2);
	// }
	// }

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		Long rlt = 0L;
		for (int i = 1; i < 1000000; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idx", i);
			map.put("group", i % 5);
			list.add(map);
		}
		System.err.println("遍历耗时：" + (System.currentTimeMillis() - start)/1000);
		MemoryGroupUtils.groupBy(list, false,"group");
		System.err.println("总共耗时："+ (System.currentTimeMillis() - start)/1000);
	}
}
