package com.iscm.core.db.splitdb;

import com.iscm.core.db.util.PropetiesUtils;
import com.iscm.vo.DbShardVO;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DbShardsUtil {

	private static int dbCount = 2;
	private static int tableCount = 1;

    public static Logger log = LoggerFactory.getLogger(DbShardsUtil.class);

	static {
		dbCount = Integer.valueOf(PropetiesUtils.getProValue("db.dbCount"));
		tableCount = Integer.valueOf(PropetiesUtils.getProValue("db.tableCount"));
	}

	private static int getDbCount() {
		return dbCount;
	}

	private static int getDbTableCount() {
		return tableCount;
	}

	/**
	 * 分库,不传参数是main公共库，如果传参数就根据所传参数组合取模，下标从0开始
	 * 
	 * @param
	 * @return
	 */
	public static Integer getJdbcIndex(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		return Math.abs(uuid.hashCode()) % getDbCount();
	}

	/**
	 * 分表，根据参数取模分表；下标从0开始
	 * 
	 * @param uuid
	 * @return
	 */
	public static Integer getTableIndex(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
			return null;
		}
		return Math.abs(uuid.hashCode()) % getDbTableCount();

	}

	public static String parseSql(String sql, String splitflag) {
		sql = sql.trim();
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		List<String> tableList = null;

		try {
			Statement stmt = CCJSqlParserUtil.parse(sql);
			if (sql.indexOf("update") == 0) {
				Update update = (Update) stmt;
				tableList = tablesNamesFinder.getTableList(update);

			} else if (sql.indexOf("insert") == 0) {
				Insert insert = (Insert) stmt;
				tableList = tablesNamesFinder.getTableList(insert);

			} else if (sql.indexOf("delete") == 0) {
				Delete delete = (Delete) stmt;
				tableList = tablesNamesFinder.getTableList(delete);
			} else {
				Select select = (Select) stmt;
				tableList = tablesNamesFinder.getTableList(select);

			}

			int suffix = getTableIndex(splitflag);
			if (suffix > 0) {
				for (String table : tableList) {
					sql = sql.replaceAll(table, table + "_" + suffix);
				}
			}
			return sql;
		} catch (Exception ex) {
			log.error(" Parse sql error：" + sql + "--" + ex.toString());
		}
		return null;
	}

	public static String getSplitTable(DbShardVO dbShardVO) {
		return dbShardVO.getShardTableId();
	}

}
