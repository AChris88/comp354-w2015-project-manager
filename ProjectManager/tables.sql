NOTE-> DATETIME insert format is: YYYY-MM-DD
ex: insert into myTable (name,mydate) Values ('fred','2007-01-01')

CREATE TABLE users(	id INTEGER PRIMARY KEY AUTOINCREMENT,
					first_name TEXT,
					last_name TEXT,
					username TEXT UNIQUE,
					password TEXT,
					salt TEXT,
					role INTEGER(1));
					
CREATE TABLE tasks(	id INTEGER PRIMARY KEY AUTOINCREMENT,
					name TEXT UNIQUE,
					projected_start DATE,
					actual_start DATE,
					projected_end DATE,
					actual_end DATE,
					to_do TEXT);
					
CREATE TABLE projects(	id INTEGER PRIMARY KEY AUTOINCREMENT,
						name TEXT,
						start_date DATE,
						projected_end DATE,
						end_date DATE);
						
CREATE TABLE project_tasks(	id INTEGER PRIMARY KEY AUTOINCREMENT,
							project_id INTEGER,
							task_id INTEGER,
							FOREIGN KEY (project_id) REFERENCES projects(id),
							FOREIGN KEY (task_id) REFERENCES tasks(id));
						
CREATE TABLE task_reqs(	id INTEGER PRIMARY KEY AUTOINCREMENT,
						task_id INTEGER,
						task_req INTEGER,
						FOREIGN KEY (task_id) REFERENCES tasks(id),
						FOREIGN KEY (task_req) REFERENCES tasks(id),
						UNIQUE id_req (task_id, task_req));
						
CREATE TABLE project_users(	id INTEGER PRIMARY KEY AUTOINCREMENT,
							project_id INTEGER,
							user_id INTEGER,
							project_role INTEGER,
							FOREIGN KEY (project_id) REFERENCES projects(id),
							FOREIGN KEY (user_id) REFERENCES users(id),
							UNIQUE project_users (project_id, user_id));
						
CREATE TABLE user_tasks(	id INTEGER PRIMARY KEY AUTOINCREMENT,
						user_id INTEGER,
						task_id INTEGER,
						project_users INTEGER,
						FOREIGN KEY (user_id) REFERENCES users(id),
						FOREIGN KEY (task_id) REFERENCES tasks(id),
						FOREIGN KEY (project_users) REFERENCES project_users(id),
						UNIQUE user_tasks(user_id, task_id));