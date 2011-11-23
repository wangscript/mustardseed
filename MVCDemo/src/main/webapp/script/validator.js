
function addErrorField(obj, name, msg) {
    if (!obj[name])
        obj[name] = new Array();
    obj[name].push(msg);
}


function main(data) {
    var ret = {};
    
    if (data["name"][0] == "") {
        addErrorField(ret, "name", "用户名不能为空");
    } else if (data["name"][0].length < 3 || data["name"][0].length > 20) {
        addErrorField(ret, "name", "用户名限制在3到20个字符");
    }
    if (data["password"][0] == "" ||
        data["password"][1] == "") {
        addErrorField(ret,"password", "两个密码均不能为空");
    } else if (data["password"][0] != data["password"][1]) {
        addErrorField(ret, "password", "两个密码必须相同");
    } else if (data["password"][0].length > 8) {
        addErrorField(ret, "password", "密码长度在必须在20个字符以内");
    }
    return ret;
}