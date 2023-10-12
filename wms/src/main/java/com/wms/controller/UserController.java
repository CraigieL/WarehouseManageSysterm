package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.service.MenuService;
import com.wms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2023-09-29
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }

    @GetMapping("/findByAccount")
    public Result findByAccount(@RequestParam String account){
        List list = userService.lambdaQuery().eq(User::getAccount,account).list();
        return list.size()>0?Result.success(list):Result.fail();
    }

    //增
    @PostMapping("/save")
    public Result save(@RequestBody User user){

        return userService.save(user)?Result.success():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody User user){ return userService.updateById(user)?Result.success():Result.fail();}

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){ return userService.removeById(id)?Result.success():Result.fail();}

    //改
    @PostMapping("/modify")
    public boolean modify(@RequestBody User user){
        return userService.updateById(user);
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    //删
    @GetMapping("/delete")
    public boolean delete(Integer id){
        return userService.removeById(id);
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List list = userService.lambdaQuery()
                .eq(User::getAccount,user.getAccount())
                .eq(User::getPassword,user.getPassword()).list();

        //return list.size()>0?Result.success(list.get(0)):Result.fail();
        if(list.size()>0){
            User user1 = (User)list.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuright,user1.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user",user1);
            res.put("menu",menuList);
            return Result.success(res);
        }
        return Result.fail();
    }


    //查询（模糊/like，匹配/eq）
    @PostMapping("/listP")
    public Result listP(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //lambdaQueryWrapper.eq(User::getName,user.getName());
        if(StringUtils.isNotBlank(user.getName())){
            lambdaQueryWrapper.like(User::getName,user.getName());
        }
        return Result.success(userService.list(lambdaQueryWrapper));
    }

    //分页
    @PostMapping("/listPage")
    public List<User> listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        //测试传入的参数
        /*System.out.println(query);*/
        System.out.println("num==="+query.getPageNum());
        System.out.println("size==="+query.getPageSize());

        /*System.out.println("name==="+(String)param.get("name"));
        System.out.println("num==="+(String)param.get("account"));
        return null;*/

        String name = (String)param.get("name");
        System.out.println("name==="+(String)param.get("name"));

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName,name);

        IPage result = userService.page(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC")
    public List<User> listPageC(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String name = (String)param.get("name");
        System.out.println("name==="+(String)param.get("name"));

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        /*LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName,name);*/

        IPage result = userService.pageC(page);

        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageCC")
    public List<User> listPageCC(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String name = (String)param.get("name");
        System.out.println("name==="+(String)param.get("name"));

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName,name);

        IPage result = userService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();

        String name = (String)param.get("name");
        String sex = (String)param.get("sex");
        String roleId = String.valueOf(param.get("roleId"));
        //System.out.println("name==="+(String)param.get("name"));

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(User::getName,name);
        }
        if (StringUtils.isNotBlank(sex) ){
            lambdaQueryWrapper.eq(User::getSex,sex);
        }
        if (StringUtils.isNotBlank(roleId) ){
            lambdaQueryWrapper.eq(User::getRoleId,roleId);
        }

        IPage result = userService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return Result.success(result.getRecords(),result.getTotal());
    }

}
