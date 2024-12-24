package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Изначальный способ изменяет список, при каждом вызове на одном и том же списке удаляет первый элемент
  // skip позволяет избегать изменение списка
  public List<String> getNames(List<Person> persons) {
    return persons.stream().
            skip(1).
            map(Person::firstName)
            .collect(Collectors.toList());
  }

  // Нет необходимости делать через стрим, т.к. в сете и так будут уникальные имена
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
  }

  // Убраны много if`ов, всё сделано через стрим, убрано дублирование secondName, объекты Null не будут обработаны
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream()
            .collect(Collectors.toMap(Person::id, this::convertPersonToString, (exist, cur) -> exist));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    return new HashSet<>(persons1).stream().anyMatch(new HashSet<>(persons2)::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    //count = 0;
    //numbers.filter(num -> num % 2 == 0).forEach(num -> count++);

    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  /*
  В данном случае всегда будет true, т.к. размер HashSeе будет рассчитан для изначального массива так, чтобы для
  каждого элемента хватила места в хеш-сете. Т.к. на вход подаются числа, то хешем для них будут являться они сами.
  При возврате в сет из хеш-сета все числа будут передаваться в порядке возрастания.
   */
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
