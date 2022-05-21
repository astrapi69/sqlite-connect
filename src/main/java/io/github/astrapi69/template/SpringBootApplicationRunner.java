/**
 * The MIT License
 *
 * Copyright (C) 2020 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.template;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import io.github.astrapi69.file.create.FileFactory;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.template.entity.Template;

@SpringBootApplication
public class SpringBootApplicationRunner implements CommandLineRunner
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args)
	{
		SpringApplication.run(SpringBootApplicationRunner.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		// create the database table
		jdbcTemplate.execute(
			"CREATE TABLE IF NOT EXISTS template(id integer PRIMARY KEY, name VARCHAR(100))");

		// insert some statement
		jdbcTemplate.execute("INSERT INTO template(id,name) VALUES (1, 'Anton')");
		jdbcTemplate.execute("INSERT INTO template(id,name) VALUES (2, 'Alfred')");

		// read entities
		List<Template> templates = jdbcTemplate.query("SELECT * FROM template",
			(resultSet, rowNum) -> Template.builder().id(resultSet.getInt("id"))
				.name(resultSet.getString("name")).build());

		// print persisted entities
		templates.forEach(System.out::println);

		// clean up
		File projectDirectory = PathFinder.getProjectDirectory();
		String path = projectDirectory.getAbsolutePath();
		String databaseName = "sqlitedb.sqlite";
		File dbFile = FileFactory.newFile(projectDirectory, databaseName);
		DeleteFileExtensions.delete(dbFile);
	}

}
