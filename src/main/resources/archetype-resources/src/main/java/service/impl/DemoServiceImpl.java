package ${package}.service.impl;

import ${package}.mapper.DemoMapper;
import ${package}.pojo.Demo;
import ${package}.service.DemoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Andy
 * @version 1.0
 * @date 02/28/2019 16:32
 * @description
 */
@Service
public class DemoServiceImpl implements DemoService {

	private DemoMapper demoMapper;

	@Override
	public int create(Demo demo) {
		return demoMapper.create(demo);
	}

	@Override
	public Demo readById(String id) {
		return demoMapper.readById(id);
	}

	@Override
	public int update(Demo demo) {
		return demoMapper.update(demo);
	}

	@Override
	public int deleteById(String id) {
		return demoMapper.deleteById(id);
	}

	@Override
	public PageInfo<Demo> list(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Demo> list = demoMapper.list();
		return new PageInfo<Demo>(list);
	}
}
