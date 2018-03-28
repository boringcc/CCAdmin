<div id="sidebar" class="sidebar                  responsive">
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
				</script>

				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
					<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
						
						<button class="btn btn-info" onclick="changeMenus();" title="切换菜单">
							<i class="ace-icon fa fa-pencil"></i>
						</button>
						
						<button class="btn btn-success" title="UI实例" onclick="window.open('static/html_UI/html');">
							<i class="ace-icon fa fa-signal"></i>
						</button>

						<!-- #section:basics/sidebar.layout.shortcuts -->
						<button class="btn btn-warning" title="" id="adminzidian">
							<i class="ace-icon fa fa-book"></i>
						</button>

						<button class="btn btn-danger">
							<i class="ace-icon fa fa-cogs"></i>
						</button>

						<!-- /section:basics/sidebar.layout.shortcuts -->
					</div>

					<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
						<span class="btn btn-success"></span>

						<span class="btn btn-info"></span>

						<span class="btn btn-warning"></span>

						<span class="btn btn-danger"></span>
					</div>
				</div><!-- /.sidebar-shortcuts -->

				<ul class="nav nav-list">
					<li class="">
						<a href="main/index">
							<i class="menu-icon fa fa-tachometer"></i>
							<span class="menu-text">后台首页</span>
						</a>
						<b class="arrow"></b>
					</li>

					<li class=""  id="1">
						<a style="cursor:pointer;" class="dropdown-toggle">
							<i class="menu-icon fa fa-home blue"></i>
							<span class="menu-text">
								系统管理
							</span>
							<b class="arrow fa fa-angle-down"></b>
						</a>
						<b class="arrow"></b>
						<ul class="submenu">
							<li class="" id="1-1">
								<a style="cursor:pointer;" class = "dropdown-toggle">
									<i class="menu-icon fa fa-lock black"></i>
									权限管理
									<b class="arrow fa fa-angle-down"></b>
								</a>
								<b class="arrow"></b>
								<ul class="submenu">
									<li class="" id="1-1-1">
										<a style="cursor:pointer;" class = "dropdown-toggle">
											<i class="menu-icon fa fa-lock orange"></i>
											角色(基础权限)
										</a>
									</li>
									<li class="" id="1-1-2">
										<a style="cursor:pointer;" class = "dropdown-toggle">
											<i class="menu-icon fa fa-lock green"></i>
											按钮权限
										</a>
									</li>
								</ul>
							</li>
							<li class="" id="1-2">
								<a style="cursor:pointer;" class = "dropdown-toggle">
									<i class="menu-icon fa fa-download orange"></i>
									按钮管理
								</a>
							</li>
							<li class="" id="1-3">
								<a style="cursor:pointer;" class = "dropdown-toggle">
									<i class="menu-icon fa fa-folder-open-o brown"></i>
									菜单管理


								</a>
							</li>
						</ul>
					</li>

					<li class=""  id="2">
						<a style="cursor:pointer;" class="dropdown-toggle">
							<i class="menu-icon fa fa-users blue"></i>
							<span class="menu-text">
								用户管理
							</span>
							<b class="arrow fa fa-angle-down"></b>
						</a>
						<b class="arrow"></b>
						<ul class="submenu">
							<li class="" id="2-1">
								<a style="cursor:pointer;" class = "dropdown-toggle" target="mainFrame" onclick="siMenu('fhindex','fhindex','系统用户','user/listUsers.do')">
									<i class="menu-icon fa fa-users green"></i>
									系统用户
								</a>
							</li>
							<li class="" id="2-2">
								<a style="cursor:pointer;" class = "dropdown-toggle">
									<i class="menu-icon fa fa-users orange"></i>
									会员管理
								</a>
							</li>
					</li>
				</ul><!-- /.nav-list -->

				<!-- #section:basics/sidebar.layout.minimize -->
				<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
					<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
				</div>

				<!-- /section:basics/sidebar.layout.minimize -->
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script>
			</div>