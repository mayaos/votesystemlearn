package com.eastcompeace.base;

public class LogType {
	public enum Logenum {
		// 利用构造函数传参
		ADDINFO(1), // 添加
		EDITINFO(2), // 修改
		DELETEINFO(3), // 删除
		SHOWINFO(4), // 详情
		EXPORTEXCLE(5), // 导出
		REGISTER(6), // 注册
		IMPORTEXCLE(7), // 导入
		DOWNLOADTEMP(8), // 模板下载
		PASSINFO(9), // 修改密码
		VOTED(10), // 发起
		QUERYINFO(11), // 查审
		FINISHINFO(12), // 完成
		OPEN(13), // 公开
		EMPOWER(14), // 分配菜单
		EMBUTTON(15), // 分配按钮
		GRANTUSER(16), // 分配角色
		CLEARINFO(17),// 清空
		QUERYLIST(18), // 查询分页
		LOGIN(19), // 登录
		LOGOUT(20), // 登出
		BACKUP(21), // 归档
		AUTH(22), // 认证
		REPLY(23); // 信息回馈
		// 定义私有变量
		private int nCode;
		// 构造函数，枚举类型只能为私有
		private Logenum(int _nCode) {
			this.nCode = _nCode;
		}

		@Override
		public String toString() {
			return String.valueOf(this.nCode);
		}

	}
}
