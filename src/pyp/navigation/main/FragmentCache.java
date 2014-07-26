package pyp.navigation.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.support.v4.app.Fragment;

/**
 * @Title: FragmentCache
 * @Description: Fragment缓存池 {类名.包名 , Fragment对象}
 * @author qsuron
 * @date 2014-7-19
 * @email admin@qiushurong.cn
 */
public class FragmentCache extends HashMap<String, Fragment> {

	private static final long serialVersionUID = 1L;
	
	// 不要缓存的Fragment列表
	private static final ArrayList<String> noCache
		= new ArrayList<String>(Arrays.asList(""));

	/**
	 * 方法 isExist 是否存在此键
	 * @param string
	 *            键(包名.类名)
	 * @return boolean
	 */
	public boolean isExist(String string) {
		return this.containsKey(string);
	}

	/**
	 * 方法 isExist 是否存在此对象
	 * 
	 * @param fragment
	 *            对象
	 * @return boolean
	 */
	public boolean isExist(Fragment fragment) {
		return this.containsValue(fragment);
	}

	/**
	 * 方法 putCache 放入到缓存中
	 * 
	 * @param string
	 * @param fragment
	 * @return
	 */
	public Fragment putCache(String string) {
		Fragment tempFragment = null;
		// 如果目前没缓存
		if (!this.isExist(string)) {
			try {
				// 反射机制-实例化需要的Fragment
				tempFragment = (Fragment) Class.forName(string).newInstance();
				// 如果不在[不缓存列表]中，就放进缓存区
				if (!noCache.contains(string)) {
					this.put(string, tempFragment);
				}
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return tempFragment;
	}

	/**
	 * 方法 getCache 获取缓存的对象
	 * 
	 * @param string
	 * @return Fragment
	 */
	public Fragment getCache(String string) {
		return this.get(string);
	}

	/**
	 * 方法 getFragmentCache 获取Fragment缓存池
	 * 
	 * @return FragmentCache
	 */
	public FragmentCache getFragmentCache() {
		return this;
	}

}
