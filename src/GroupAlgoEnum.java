import java.util.List;
import java.util.Map;

public enum GroupAlgoEnum implements IGroupAlgo<Map<String, Object>> {

	AVG {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			
		}
	}, // ƽ����

	ROW2COLUMN {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}, // ��ת��

	SUM {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}, // ���

	COUNT {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}, // ����

	MAX {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}, // ���ֵ

	MIN {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}; // ��Сֵ

	private GroupAlgoEnum() {

	}

}
