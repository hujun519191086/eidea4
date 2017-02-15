package com.dsdl.eidea.common.web.controller;

import com.dsdl.eidea.base.entity.bo.PageMenuBo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bobo on 2016/12/23 9:07.
 */
@Controller
@RequestMapping("/common/getLeftMenus")
public class GetLeftMenusController {


    /**
     * getLeftMenuList:左侧菜单列表
     * @return
     */
    @RequestMapping(value = "/getLeftMenuList",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getLeftMenuList(HttpServletRequest request){
        String serverName=request.getServletContext().getContextPath();
        PageMenuBo pageMenuBo=new PageMenuBo();
        pageMenuBo.setServerName(serverName);
        String menuString="";
        ModelAndView modelAndView=new ModelAndView("/index");
        modelAndView.addObject("menuString",menuString);
        return  modelAndView;
    }

}
