package com.util.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

@Repository("dao")
public class CommonDAOImpl implements CommonDAO {

	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;
	
/*	@Autowired 때문에 없어도 실행된다. 
 * 알아서 sqlMapClientTemplate을 찾아온다 
 
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate=sqlMapClientTemplate;
	}
*/
	
	
	@Override
	public void insertData(String id, Object value) {
		try {
		
			
			sqlMapClientTemplate.insert(id, value);	 
			
		
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				
			} catch(Exception e) {}
		}
	}

	@Override
	public int updateData(String id, Map<String, Object> map) {
		int result = 0;
		try {
		
			result = sqlMapClientTemplate.update(id, map);
	
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
			} catch(Exception e) {}
		}
		
		return result;
	}

	@Override
	public int updateData(String id, Object value) {
		int result = 0;
		try {
			
			
			result = sqlMapClientTemplate.update(id, value);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {

			} catch(Exception e) {}
		}
		
		return result;
	}


	@Override
	public int deleteData(String id, Object value) {
		int result = 0;
		try {

			result = sqlMapClientTemplate.delete(id, value);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
			} catch(Exception e) {}
		}
		
		return result;
	}

	@Override
	public int deleteData(String id, Map<String, Object> map) {
		int result = 0;
		try {
			
			result = sqlMapClientTemplate.delete(id, map);

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
			} catch(Exception e) {}
		}
		
		return result;
	}

	@Override
	public int deleteAllData(String id) {
		int result = 0;
		try {
			
			result = sqlMapClientTemplate.delete(id);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {

			} catch(Exception e) {}
		}
		
		return result;
	}

	@Override
	public Object getReadData(String id, Object value) {
		try {
			return sqlMapClientTemplate.queryForObject(id, value);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return null;
	}

	@Override
	public Object getReadData(String id) {
		try {
			return sqlMapClientTemplate.queryForObject(id);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	@Override
	public Object getReadData(String id, Map<String, Object> map) {
		try {
			return sqlMapClientTemplate.queryForObject(id, map);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	@Override
	public int getIntValue(String id) {
		try {
			return ((Integer)sqlMapClientTemplate.queryForObject(id)).intValue();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return 0;
	}
	
	@Override
	public int getIntValue(String id, Object value) {
		try {
			return ((Integer)sqlMapClientTemplate.queryForObject(id, value)).intValue();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return 0;
	}

	@Override
	public int getIntValue(String id, Map<String, Object> map) {
		try {
			return ((Integer)sqlMapClientTemplate.queryForObject(id, map)).intValue();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getListData(String id) {
		try {
			return (List<Object>) sqlMapClientTemplate.queryForList(id);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getListData(String id, Object value) {
		try {
			return (List<Object>) sqlMapClientTemplate.queryForList(id, value);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getListData(String id, Map<String, Object> map) {
		try {
			return (List<Object>) sqlMapClientTemplate.queryForList(id, map);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
}
