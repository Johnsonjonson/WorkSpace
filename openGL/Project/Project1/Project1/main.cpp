#include <glad/glad.h>
#include <GLFW/glfw3.h>
#include <iostream>

// ������ɫ��Դ����
const char *vertexShaderSource = "#version 330 core\n"
	"layout (location =0) in vec3 aPos;\n"
	"void main()\n"
	"{\n"
	"	gl_Position = vec4(aPos.x,aPos.y,aPos.z,1.0);\n"
	"}\0";

// Ƭ����ɫ��Դ����
const char *fragmentShaderSource = "#version 330 core\n"
	"out vec4 FragColor;\n"
	"uniform vec4 ourColor;\n"
	"void main()\n"
	"{\n"
	//"	FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n"
	"	FragColor = ourColor;\n"
	"}\0";

// Ƭ����ɫ��2
const char *fragmentShaderSource2 = "#version 330 core\n"
	"out vec4 FragColor;\n"
	"void main()\n"
	"{\n"
	"	FragColor = vec4(1.0f, 1.0f, 0.0f, 1.0f);\n"
	"}\0";

void framebuffer_size_callback(GLFWwindow* window, int width, int height);
void processInput(GLFWwindow *window);

int main() {
	// ��ʼ��GLFW
	glfwInit();
	//����GLFW
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	//glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);   //Mac OS Xϵͳ
	//
	//����һ�����ڶ���
	GLFWwindow* window = glfwCreateWindow(800, 600, "LearnOpenGL", NULL, NULL);
	if (window == NULL) {
		std::cout << "Failed to create GLFW window" << std::endl;
		glfwTerminate();
	}
	//
	glfwMakeContextCurrent(window);
	if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress))
	{
		//std::cout << "Failed to initialize GLAD" << std::endl;
		return -1;
	}
	glViewport(0, 0, 800, 600);
	glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);

	// ����������ɫ��������
	unsigned int vertexShader;
	vertexShader = glCreateShader(GL_VERTEX_SHADER); //����������ɫ��
	glShaderSource(vertexShader, 1, &vertexShaderSource, NULL);
	glCompileShader(vertexShader);

	// ��ⶥ����ɫ���Ƿ����ɹ���
	int  success;
	char infoLog[512];
	glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &success);
	if (!success)
	{
		glGetShaderInfoLog(vertexShader, 512, NULL, infoLog);
		std::cout << "ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" << infoLog << std::endl;
	}


	// ��������ɫ���������ӵ�һ��������Ⱦ����ɫ������(Shader Program)��

	// ����Ƭ����ɫ��������
	unsigned int fragmentShader;
	fragmentShader = glCreateShader(GL_FRAGMENT_SHADER); //����Ƭ����ɫ��
	glShaderSource(fragmentShader, 1, &fragmentShaderSource, NULL);
	glCompileShader(fragmentShader);
	
	// ����Ƭ����ɫ��������2
	unsigned int fragmentShader2;
	fragmentShader2 = glCreateShader(GL_FRAGMENT_SHADER); //����Ƭ����ɫ��2
	glShaderSource(fragmentShader2, 1, &fragmentShaderSource2, NULL);
	glCompileShader(fragmentShader2);

	// ���Ƭ����ɫ���Ƿ����ɹ���
	glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &success);
	if (!success)
	{
		glGetShaderInfoLog(fragmentShader, 512, NULL, infoLog);
		std::cout << "ERROR::SHADER::FRAGMENT::COMPILATION_FAILED\n" << infoLog << std::endl;
	}

	// ����һ��������� ������������ɫ��
	unsigned int shaderProgram;
	shaderProgram = glCreateProgram();

	glAttachShader(shaderProgram, vertexShader);
	glAttachShader(shaderProgram, fragmentShader);
	glLinkProgram(shaderProgram);

	glGetProgramiv(shaderProgram, GL_LINK_STATUS, &success);
	if (!success) {
		glGetProgramInfoLog(shaderProgram, 512, NULL, infoLog);
		std::cout << "ERROR::SHADER::PROGRAM::COMPILATION_FAILED\n" << infoLog << std::endl;
	}
	// ����������
	glUseProgram(shaderProgram);

	// ����һ���������2 ������������ɫ��
	unsigned int shaderProgram2;
	shaderProgram2 = glCreateProgram();

	glAttachShader(shaderProgram2, vertexShader);
	glAttachShader(shaderProgram2, fragmentShader2);
	glLinkProgram(shaderProgram2);

	// ����������2
	glUseProgram(shaderProgram2);


	glDeleteShader(vertexShader);
	glDeleteShader(fragmentShader);



	/*
	// 0. ���ƶ������鵽�����й�OpenGLʹ��
	glBindBuffer(GL_ARRAY_BUFFER, VBO);
	glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
	// 1. ���ö�������ָ��
	//ʹ��glVertexAttribPointer��������OpenGL����ν�����������
	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
	glEnableVertexAttribArray(0);
	// 2. ��������Ⱦһ������ʱҪʹ����ɫ������
	glUseProgram(shaderProgram);
	// 3. ��������
	//someOpenGLFunctionThatDrawsOurTriangle();
	*/

	// ���嶥������
/* ������
float vertices[] = {
	-0.5f, -0.5f, 0.0f,
	 0.5f, -0.5f, 0.0f,
	 0.0f,  0.5f, 0.0f
};
*/


	float vertices[] = {
		0.5f, 0.5f, 0.0f,   // ���Ͻ�
		0.5f, -0.5f, 0.0f,  // ���½�
		-0.5f, -0.5f, 0.0f, // ���½�
		-0.5f, 0.5f, 0.0f   // ���Ͻ�
	};

	unsigned int indices[] = { // ע��������0��ʼ! 
		0, 1, 3, // ��һ��������
		1, 2, 3  // �ڶ���������
	};

	/*
		�����������Vertex Array Object��VAO
		���㻺�����Vertex Buffer Object��VBO
		�����������Element Buffer Object��EBO��Index Buffer Object��IBO
	*/

	// �������㻺�����
	unsigned int VBO;
	glGenBuffers(1, &VBO);

	//���������������
	unsigned int VAO;
	glGenVertexArrays(1, &VAO);

	//�����������
	unsigned int EBO;
	glGenBuffers(1, &EBO);


	// 1. ��VAO
	glBindVertexArray(VAO);
	// 2. �Ѷ������鸴�Ƶ������й�OpenGLʹ��
	glBindBuffer(GL_ARRAY_BUFFER, VBO);
	glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
	// 3. �������ǵ��������鵽һ�����������У���OpenGLʹ��
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);
	// 4. ���ö�������ָ��   ����OpenGL����ν�����������
	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
	//���ö�������,��������Ĭ���ǽ��õ�
	glEnableVertexAttribArray(0);

	//ʹ���߿�ģʽ  glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)�������û�Ĭ��ģʽ
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	while (!glfwWindowShouldClose(window))
	{
		processInput(window);

		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		// 5. ��������
		glUseProgram(shaderProgram);
		float timeValue = glfwGetTime();
		float greenValue = sin(timeValue) / 2.0f + 0.5f;
		int vertexColorLocation = glGetUniformLocation(shaderProgram, "ourColor");
		glUniform4f(vertexColorLocation, 0.0f, greenValue, 0.0f, 1.0f);
		glBindVertexArray(VAO);
		//glDrawArrays(GL_TRIANGLES, 0, 3);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

		glUseProgram(shaderProgram2);
		glDrawArrays(GL_TRIANGLES, 0, 3);
		//glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

		glBindVertexArray(0);

		//glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);

		glfwSwapBuffers(window);
		glfwPollEvents();
	}


	// optional: de-allocate all resources once they've outlived their purpose:
	// ------------------------------------------------------------------------
	glDeleteVertexArrays(1, &VAO);
	glDeleteBuffers(1, &VBO);
	glDeleteBuffers(1, &EBO);
	glDeleteProgram(shaderProgram);

	glfwTerminate();
	return 0;
}

void framebuffer_size_callback(GLFWwindow* window, int width, int height)
{
	glViewport(0, 0, width, height);
}

void processInput(GLFWwindow *window) {
	if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
		glfwSetWindowShouldClose(window, true);
	}
}