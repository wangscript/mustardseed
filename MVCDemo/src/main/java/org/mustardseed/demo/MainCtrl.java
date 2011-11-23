package org.mustardseed.demo;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMethod;

//文件上传相关
import org.springframework.web.multipart.MultipartFile;
//数据校验相关
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

//文件管理
import org.mustardseed.filemanager.FileManager;
import org.mustardseed.filemanager.FileNode;
import javax.annotation.Resource;

//表单验证
import org.mustardseed.script.javascript.JSEngine;
import org.mozilla.javascript.Scriptable;
import org.mustardseed.validation.FormValidResult;


//定义该类为控制器Bean
//定义控制器所关联的路径
@Controller
@RequestMapping(value="/example")
public class MainCtrl {

    private FileManager fileManager;
    
    //将"index.do"的处理方法定义为改成员方法,并绑定METHOD为GET
    @RequestMapping(value="/index", 
		    method=RequestMethod.GET)
    public ModelAndView index() {
	//定义所使用的视图
	ModelAndView mav = new ModelAndView();
	//定义视图名
	mav.setViewName("index");
	//为视图添加所需要的参数
	mav.addObject("message", "中文");
	mav.addObject("fileList", 
		      fileManager.listNodes(fileManager.getRootNodeId()));
	//返回视图
	return mav;
    }

    //Path参数演示
    @RequestMapping(value="/path_var/{id}/{word}")
    public ModelAndView pathVar(//将路径变量id绑定在参数id上
				@PathVariable("id") int id,
				//将路径变量word绑定在参数word上
				@PathVariable("word") String word) {
	ModelAndView mav = new ModelAndView();
	mav.setViewName("path_var");
	mav.addObject("id", id);
	mav.addObject("word", word);
	return mav;
    }

    //param参数GET获取演示
    @RequestMapping(value="/param_var",
		    method=RequestMethod.GET)
    public ModelAndView paramVarGet(//将param变量msg绑定在message上
				    @RequestParam("msg") String message) {
	ModelAndView mav = new ModelAndView();
	mav.setViewName("param_var");
	mav.addObject("msg", "GET=>" + message);
	return mav;
    }

    //param参数POST获取演示
    @RequestMapping(value="/param_var",
		    method=RequestMethod.POST)
    public ModelAndView paramVarPost(//将param变量msg绑定在message上
				     @RequestParam("msg") String message) {
	ModelAndView mav = new ModelAndView();
	mav.setViewName("param_var");
	mav.addObject("msg", "POST=>" + message);
	return mav;
    }

    //对象映射参数演示
    @RequestMapping(value="/obj_var",
		    method=RequestMethod.POST)
    public ModelAndView objVar(//将表单的参数绑定到相关的对象参数上
			       @ModelAttribute TestObject obj) {
	ModelAndView mav = new ModelAndView();
	mav.setViewName("obj_var");
	mav.addObject("obj", obj);
	return mav;
    }
    
    //文件上传演示
    
    @RequestMapping(value="/upload",
		    method=RequestMethod.POST)
    public ModelAndView upload(//将上传文件类型绑定
			       @RequestParam("file") MultipartFile file) {
	ModelAndView mav = new ModelAndView();
	//获取远程文件名
	String name = file.getOriginalFilename();
	//获取contentType
	String type = file.getContentType();
	
	mav.setViewName("file");
	mav.addObject("name", name);
	mav.addObject("type", type);
	//将文件保存至服务器
	fileManager.putFileNode(fileManager.getRootNodeId(),file);
	return mav;
    }

    //数据校验
    //绑定校验器，可一次绑定多个
    @InitBinder
    public void initBinder(WebDataBinder binder) {
	binder.setValidator(new TempValid());
    }
    @RequestMapping(value="/err")
    public ModelAndView error(@Valid //标注需要校验的数据
			      @ModelAttribute TestObject obj, 
			      //用于捕获数据校验的结果
			      BindingResult result) {
	ModelAndView mav = new ModelAndView();
	mav.setViewName("err");
	//判断数据校验是否成功
	if (result.hasErrors())
	    mav.addObject("errmsg", "校验出错");
	else
	    mav.addObject("errmsg", "无任何错误");
	return mav;
    }

	
    
    //AJAX演示
    @RequestMapping(value="/ajax",
		    method=RequestMethod.POST)
    @ResponseBody
    public Map ajax(@RequestParam("cnt") int cnt) {
	Map mav = new HashMap();
	mav.put("cnt",cnt + 1);
	mav.put("msg","中文");
	return mav;
    }
    
    //文件下载示例
    @RequestMapping(value="/download")
    public ModelAndView download(@RequestParam("id") String id) 
	throws Exception {
	ModelAndView mav = new ModelAndView();
	FileNode node = fileManager.getNode(id);
	if (node != null && node.isFile()) {
	    String path = fileManager.getNodeFileURI(node.getId()).getPath();
	    //定义视图为二进制下载视图
	    mav.setViewName(":binaryView:");
	    //(必须)定义下载的二进制内容，为本地文件路径
	    mav.addObject("path", path);
	    //(可选)定义文件名
	    mav.addObject("filename", node.getName());
	    //(可选)定义 inline, attechment
	    mav.addObject("position", "attechment");
	    //(可选)定义流的类型
	    //mav.addObject("contentType", "plain/text");
	} else {
	    mav.setViewName("redirect:index.do");
	}


	return mav;
    }
    
    //表单验证示例(表单页面显示)
    @RequestMapping(value="/validation",
		    method=RequestMethod.GET)
    public String validation() {
	return "form";
    }
    //表单验证示例(表单处理部分)
    @RequestMapping(value="/validation",
		    method=RequestMethod.POST)
    public ModelAndView validation(@RequestParam("name") String name,
				   @RequestParam("password") String password,
				   //保存表单验证结果的Map
				   @FormValidResult("main") Map result) {
	ModelAndView mav = new ModelAndView();
	//如果表单验证失败
	if (result != null) {
	    mav.setViewName("form");
	    mav.addObject("errors", result);
	    mav.addObject("name", name);
	    mav.addObject("password", password);
	} else {
	    mav.setViewName("redirect:index.do");
	}
	return mav;
    }

    @Resource
    public void setFileManager(FileManager fileManager) {
	this.fileManager = fileManager;
    }
}