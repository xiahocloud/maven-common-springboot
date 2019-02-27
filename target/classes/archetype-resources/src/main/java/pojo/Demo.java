package $

import java.util.Objects;{package}.pojo;

import java.util.Objects;

/**
 * @author Andy
 * @version 1.0
 * @date 02/27/2019 15:48
 * @description Demo
 */
public class Demo {
	private String name;

	public Demo() {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof com.congitationsoft.mavencommonspringboot.Demo)) {
			return false;
		}
		com.congitationsoft.mavencommonspringboot.Demo demo = (com.congitationsoft.mavencommonspringboot.Demo) o;
		return Objects.equals(getName(), demo.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

	@Override
	public String toString() {
		return "Demo{" +
				"name='" + name + '\'' +
				'}';
	}
}
