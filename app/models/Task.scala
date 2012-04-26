package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Task(id : Long , label : String) 

object Task{
  
  //这个task是一个解析器，给一个JDBC的ResultSet(至少含有一个id和一个label列)，就可以创建出一个Task值
  val task = {
    get[Long]("id") ~
    get[String]("label") map{
      case id~label => Task(id , label)
    }
  }
  
  //使用DB.withConnection帮助类来自动创建和释放一个JDBC连接
  def all() : List[Task] = DB.withConnection{implicit c =>
    SQL("select * from task").as(task *)    //as 会解析可能多的task列，然后返回一个List[Task]
  }
  
  def create(label : String) {
    DB.withConnection{implicit c =>
      SQL("insert into task (label) values ({label})").on('label -> label).executeUpdate()     
    }
  }
  
  def delete(id : Long){
    DB.withConnection{ implicit c =>
      SQL("delete from task where id = {id}").on('id -> id).executeUpdate 
      
    }
    
  }
  
}