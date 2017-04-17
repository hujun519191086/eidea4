<#include "../inc/class_header.ftl"/>

package ${basePackage}.${module}.web.controller;

import ${basePackage}.${module}.entity.po.${model}Po;
import ${basePackage}.${module}.service.${model}Service;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.dsdl.eidea.core.web.def.WebConst;
import com.dsdl.eidea.core.web.result.JsonResult;
import com.dsdl.eidea.core.web.result.def.ErrorCodes;
import com.dsdl.eidea.core.web.util.SearchHelper;
import com.dsdl.eidea.core.web.vo.PagingSettingResult;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
* Created by 刘大磊 on ${datetime}.
*/
@Controller
@RequestMapping("/${module}/${model}")
public class ${model}Controller extends BaseController {
private static final String URI = "${model?uncap_first}";
@Autowired
private ${model}Service ${model?uncap_first}Service;
@RequestMapping(value = "/showList", method = RequestMethod.GET)
@RequiresPermissions("view")
public ModelAndView showList() {
ModelAndView modelAndView = new ModelAndView("/${module}/${model?uncap_first}/${model?uncap_first}");
<#if memPaging>
modelAndView.addObject(WebConst.PAGING_SETTINGS, PagingSettingResult.getDefault());
<#else>
modelAndView.addObject(WebConst.PAGING_SETTINGS, PagingSettingResult.getDbPaging());
</#if>
modelAndView.addObject(WebConst.PAGE_URI, URI);
return modelAndView;
}

@RequestMapping(value = "/list", method = RequestMethod.GET)
@ResponseBody
@RequiresPermissions("view")
public JsonResult<List<${model}Po>> list(HttpSession session) {
    Search search = SearchHelper.getSearchParam(URI, session);
    List<${model}Po> ${model?uncap_first}List = ${model?uncap_first}Service.get${model}List(search);
        return JsonResult.success(${model?uncap_first}List);
    }

    @RequiresPermissions("view")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult<${model}Po> get(${pkClass} id) {
        ${model}Po ${model?uncap_first}Po = null;
        if (id == null) {
        return JsonResult.fail(ErrorCodes.VALIDATE_PARAM_ERROR.getCode(),getMessage("common.errror.get_object",getLabel("${model?uncap_first}.title")));
        } else {
        ${model?uncap_first}Po = ${model?uncap_first}Service.get${model}(id);
        }
        return JsonResult.success(${model?uncap_first}Po);
        }

        @RequiresPermissions("add")
        @RequestMapping(value = "/create", method = RequestMethod.GET)
        @ResponseBody
        public JsonResult<${model}Po> create() {
            ${model}Po ${model?uncap_first}Po = new ${model}Po();
            return JsonResult.success(${model?uncap_first}Po);
            }

    /**
    * @param ${model?uncap_first}Po
    * @return
    */
    @RequiresPermissions("add")
    @RequestMapping(value = "/saveForCreated", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<${model}Po> saveForCreate(@Validated @RequestBody ${model}Po ${model?uncap_first}Po) {
        ${model?uncap_first}Service.save${model}(${model?uncap_first}Po);
        return get(${model?uncap_first}Po.get${pkName?cap_first}());
        }

        @RequiresPermissions("update")
        @RequestMapping(value = "/saveForUpdated", method = RequestMethod.POST)
        @ResponseBody
        public JsonResult<${model}Po> saveForUpdate(@Validated @RequestBody ${model}Po ${model?uncap_first}Po) {

            if(${model?uncap_first}Po.get${pkName?cap_first}() == null){
            return JsonResult.fail(ErrorCodes.VALIDATE_PARAM_ERROR.getCode(), getMessage("common.errror.pk.required"));
            }
            ${model?uncap_first}Service.save${model}(${model?uncap_first}Po);
            return get(${model?uncap_first}Po.get${pkName?cap_first}());
            }

    @RequiresPermissions("delete")
    @RequestMapping(value = "/deletes", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<List<${model}Po>> deletes(@RequestBody ${pkClass}[] ids, HttpSession session) {
        if (ids == null || ids.length == 0) {
        return JsonResult.fail(ErrorCodes.VALIDATE_PARAM_ERROR.getCode(), getMessage("common.error.delete.failure",getMessage("${model?uncap_first}.title")));
        }
        ${model?uncap_first}Service.deletes(ids);
        return list(session);
        }
}
