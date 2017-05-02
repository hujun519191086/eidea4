/**
* 版权所有 刘大磊 2013-07-01
* 作者：刘大磊
* 电话：13336390671
* email:ldlqdsd@126.com
*/
package com.dsdl.eidea.base.service.impl;

import com.dsdl.eidea.core.spring.annotation.DataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsdl.eidea.base.entity.po.CommonFilePo;
import com.dsdl.eidea.base.service.CommonFileService;
import com.dsdl.eidea.core.dto.PaginationResult;
import com.dsdl.eidea.core.params.QueryParams;
import com.googlecode.genericdao.search.SearchResult;
import com.googlecode.genericdao.search.Search;
import com.dsdl.eidea.core.dao.CommonDao;
import java.util.List;
/**
 * @author 刘大磊 2017-05-02 13:09:39
 */
@Service("commonFileService")
public class CommonFileServiceImpl  implements	CommonFileService {
	@DataAccess(entity =CommonFilePo.class)
	private CommonDao<CommonFilePo,Integer> commonFileDao;
	public PaginationResult<CommonFilePo> getCommonFileListByPaging(Search search,QueryParams queryParams)
    {
		search.setFirstResult(queryParams.getFirstResult());
		search.setMaxResults(queryParams.getPageSize());
		PaginationResult<CommonFilePo> paginationResult = null;
		if (queryParams.isInit()) {
		SearchResult<CommonFilePo> searchResult = commonFileDao.searchAndCount(search);
		paginationResult = PaginationResult.pagination(searchResult.getResult(), searchResult.getTotalCount(), queryParams.getPageNo(), queryParams.getPageSize());
		}
		else
		{
		List<CommonFilePo> commonFilePoList = commonFileDao.search(search);
		paginationResult = PaginationResult.pagination(commonFilePoList, queryParams.getTotalRecords(), queryParams.getPageNo(), queryParams.getPageSize());
		}
    	return paginationResult;
    }

    public CommonFilePo getCommonFile(Integer id)
	{
		return commonFileDao.find(id);
	}
    public void saveCommonFile(CommonFilePo commonFile)
	{
		commonFileDao.save(commonFile);
	}
    public void deletes(Integer[] ids)
	{
		commonFileDao.removeByIds(ids);
	}
}