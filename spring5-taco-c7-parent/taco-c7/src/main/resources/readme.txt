jdbc:
如果在应用的根类路径下存在名为schema.sql的文件，那么在应用启动的时候将会基于数据库执行这个文件中的SQL。
我们可能还希望在数据库中预加载一些配料数据。幸运的是，SpringBoot还会在应用启动的时候执行根类路径下名为data.sql的文件。

jpa:
需要在application.properties中配置spring.jpa.hibernate.ddl-auto=create-drop，使用H2会默认该值
不需要schema.sql，而是根据entity创建
data.sql改名为import.sql,但是插入非基本类型的会出错，建议在配置配中用CommandLineRunner执行