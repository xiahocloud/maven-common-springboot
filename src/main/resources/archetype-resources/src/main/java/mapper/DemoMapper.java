package ${package}.mapper;

import ${package}.pojo.Demo;

import java.util.List;

/**
 * @author Andy
 * @version 1.0
 * @date 02/28/2019 17:21
 * @description Mapper
 */
public interface DemoMapper {
	/**
	 * Method Description: Created by whx
	 * 〈 增加一个Demo实例 〉
	 *
	 * @param demo
	 * @return 结果
	 * @throws:
	 * @date 02/28/2019 16:13
	 */
	int create(Demo demo);

	/**
	 * Method Description: Created by whx
	 * 〈 通过I进行条件查询 〉
	 *
	 * @param id id
	 * @return Demo 结果
	 * @throws:
	 * @date 02/28/2019 16:30
	 */
	Demo readById(String id);

	/**
	 * Method Description: Created by whx
	 * 〈 更新操作 〉
	 *
	 * @param demo demo
	 * @return 返回结果
	 * @throws:
	 * @date 02/28/2019 16:31
	 */
	int update(Demo demo);

	/**
	 * Method Description: Created by whx
	 * 〈 删除操作 〉
	 *
	 * @param id id
	 * @return 结果
	 * @throws:
	 * @date 02/28/2019 16:31
	 */
	int deleteById(String id);

	/**
	 * Method Description: Created by whx
	 * 〈 查询整个Demo列表 〉
	 *
	 * @return 列表
	 * @throws:
	 * @date 02/28/2019 17:34
	 */
	List<Demo> list();
}
