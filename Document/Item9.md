# try-finally보다는 try-with-resource를 사용하라

> 자바 라이브러리에는 close메소드를 호출하여 직접 닫아줘야 하는 자원이 많다.
>
> `InputStream`, `OutputStream`, `java.sql.connection` 등이 좋은 예다. 이러한 자원 닫기는
>
> 클라이언트가 놓치기 쉬워 예측할 수 없는 성능 문제로 이어지기도 한다.



전통적으로 자원이 제대로 닫힘을 보장하는 수단으로 try-finally가 쓰였다.

```java
static String firstLineOfFile(String path) throws IOException {
  BufferedReader br = new BufferedReader(new FileReader(path));
  try {
    return br.readLine();
  } finally {
    br.close();
  }
}
```

그러나, 자원이 두가지가 될 경우에?

~~~java
static void copy(String src, String dst) throws IOExecption {
  InputStream in = new FileInputStream(src);
  try {
    OutputStream out = new FileOutputStream(dst);
    try {
      byte[] buf = new byte[BUFFER_SIZE];
      int n;
      while((n = in.read(buf)) >= 0)
        out.write(buf, 0, n);
    } finally {
      out.close()
    }
  } finally {
    in.close();
  }
}
~~~

> 코드의 depth가 깊어지면서, 복잡한 코드가 되어버린다.

위 상황에서는 총 두번의 에러가 발생 할 수 있다. 하지만 여기서 문제점은 에러메시지가 두번째 예외가 첫번째 예외를

집어삼켜버린다. 그래서 첫번째 예외에 관한 메시징이 스택 추적 내역에 남지 않게 된다. 이는

디버깅을 매우 어렵게 만든다.



이를 해결하는 자바7의 `try-with-resource`

이 구조를 사용하려면 해당 자원이 AutoCloseable인터페이스를 구현해야한다. Try-with-resource로 close() 가

호출되는 객체는 Autocloseable을 구현한 객체만 close()가 호출되기 때문이다.

> 자바 라이브러리와 서드파티 라이브러리들의 수많은 클래스와 인터페이스가 이미 AutoCloseable을
>
> 구현하거나 확장해두었다. 사용하기전 Autocloseable이 구현되었는지 확인해보자

~~~java
static String firstLineofFile(String path) throws IOException {
  try (BufferedReader br = new BufferedReader(new FileReader(path))) {
    return br.readLine();
  }
}
~~~

복수자원에서는?

~~~java
static void copy(String src, String dst) throws IOExecption {
  try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
    byte[] buf = new byte[BUFFER_SIZE];
    int n;
    whlie((n = in.read(buf)) >= 0) 
      out.write(buf, 0, n);
  }
}
~~~

> 훨씬 depth도 얕고, 읽기 좋은 코드가 되었다.

위처럼 예외를 스택 추적 내역에서 버리지않고, 다른 예외가 숨겨졌다는`suppressed` 가 출력된다.

