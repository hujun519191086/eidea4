package com.dsdl.eidea.base.service.impl;


import com.dsdl.eidea.base.dao.*;
import com.dsdl.eidea.base.entity.bo.ModuleBo;
import com.dsdl.eidea.base.entity.bo.ModuleDirectoryBo;
import com.dsdl.eidea.base.entity.bo.ModuleMenuBo;
import com.dsdl.eidea.base.entity.po.*;
import com.dsdl.eidea.base.service.ModuleService;
import com.dsdl.eidea.core.dao.BaseDao;
import com.googlecode.genericdao.search.Search;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bobo on 2016/12/14 8:53.
 */
@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private PageMenuDao pageMenuDao;
    @Autowired
    private DirectoryDao directoryDao;
    @Autowired
    private ModuleMenuDao moduleMenuDao;
    @Autowired
    private ModuleDirectoryDao moduleDirectoryDao;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ModuleBo> getModuleList(Search search) {
        List<ModulePo> modulePoList=moduleDao.search(search);
        return modelMapper.map(modulePoList, new TypeToken<List<ModuleBo>>() {}.getType());
    }

    @Override
    public void deleteModuleList(Integer[] ids) {
        moduleDao.removeByIdsForLog(ids);
    }

    @Override
    public void saveModule(ModuleBo moduleBo) {
        if(moduleBo.getIsactive() == null){
            moduleBo.setIsactive("N");
        }
        ModulePo modulePo=modelMapper.map(moduleBo,ModulePo.class);
        Search search=new Search();
        if(moduleBo.getMenuIds().length > 0){
            search.addFilterIn("sysModule.id",moduleBo.getId());
            List<ModuleMenuPo> moduleMenuAllList=moduleMenuDao.search(search);
            if(moduleMenuAllList != null && moduleMenuAllList.size() > 0){
                for(ModuleMenuPo moduleMenu:moduleMenuAllList){
                    moduleMenuDao.removeForLog(moduleMenu);
                }
            }
            List<ModuleMenuPo> moduleMenuList = new ArrayList<ModuleMenuPo>();
            for (Integer menuId:moduleBo.getMenuIds()){
                ModuleMenuPo moduleMenuPo=new ModuleMenuPo();
                PageMenuPo pageMenuPo=pageMenuDao.find(menuId);
                moduleMenuPo.setSysPageMenu(pageMenuPo);
                moduleMenuPo.setSysModule(modulePo);
                moduleMenuList.add(moduleMenuPo);
            }
            modulePo.setSysModuleMenus(moduleMenuList);
        }
        if(moduleBo.getDirIds().length > 0){
            search.addFilterIn("sysModule.id",moduleBo.getId());
            List<ModuleDirectoryPo> moduleDirectoryAllList=moduleDirectoryDao.search(search);
            if(moduleDirectoryAllList != null && moduleDirectoryAllList.size() > 0){
                for(ModuleDirectoryPo moduleDirectory:moduleDirectoryAllList){
                    moduleDirectoryDao.removeForLog(moduleDirectory);
                }
            }
            List<ModuleDirectoryPo> moduleDirectoryList = new ArrayList<ModuleDirectoryPo>();
            for(Integer dirId:moduleBo.getDirIds()){
                ModuleDirectoryPo moduleDirectoryPo=new ModuleDirectoryPo();
                DirectoryPo directoryPo=directoryDao.find(dirId);
                moduleDirectoryPo.setSysDirectory(directoryPo);
                moduleDirectoryPo.setSysModule(modulePo);
                moduleDirectoryList.add(moduleDirectoryPo);
            }
            modulePo.setSysModuleDirectories(moduleDirectoryList);
        }
        moduleDao.saveForLog(modulePo);
        moduleBo.setId(modulePo.getId());
    }

    @Override
    public ModuleBo getModule(int id) {
        ModulePo modulePo= moduleDao.find(id);
        ModuleBo moduleBo=modelMapper.map(modulePo,ModuleBo.class);
        if(modulePo != null){
            List<ModuleMenuBo> moduleMenuBoList=modelMapper.map(modulePo.getSysModuleMenus(),new TypeToken<List<ModuleMenuBo>>() {}.getType());
            if(moduleMenuBoList != null && moduleMenuBoList.size() > 0){
                Integer[] ids=new Integer[moduleMenuBoList.size()];
                for(int i=0;i<moduleMenuBoList.size();i++){
                    ids[i]=moduleMenuBoList.get(i).getSysPageMenuId();
                }
                moduleBo.setMenuIds(ids);
            }
            List<ModuleDirectoryBo> moduleDirectoryBoList=modelMapper.map(modulePo.getSysModuleDirectories(),new TypeToken<List<ModuleDirectoryBo>>() {}.getType());
            if(moduleDirectoryBoList != null && moduleDirectoryBoList.size() > 0){
                Integer[] ids=new Integer[moduleDirectoryBoList.size()];
                for(int i=0;i<moduleDirectoryBoList.size();i++){
                    ids[i]=moduleDirectoryBoList.get(i).getSysDirectoryId();
                }
                moduleBo.setDirIds(ids);
            }
        }
        return moduleBo;
    }

    @Override
    public boolean findExistId(Integer id) {
        ModulePo modulePo=  moduleDao.find(id);
        if (modulePo != null) {
            return true;
        } else {
            return false;
        }
    }
}
