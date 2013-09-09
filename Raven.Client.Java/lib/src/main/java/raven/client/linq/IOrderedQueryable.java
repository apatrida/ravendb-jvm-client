package raven.client.linq;

import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
//TODO java doc
public interface IOrderedQueryable<T> extends Iterable<T> {
  /**
   * Filters a sequence of values based on a predicate.
   * @param predicate
   * @return
   */
  public IRavenQueryable<T> where(Predicate predicate);

  /**
   * Changes order of result elements
   * @param asc
   * @return
   */
  public IRavenQueryable<T> orderBy(OrderSpecifier<?>... asc);

  public List<T> toList();

  public IRavenQueryable<T> skip(int itemsToSkip);

  public IRavenQueryable<T> take(int amount);

  public T first();

  public T firstOrDefault();

  public T single();

  public T singleOrDefault();

  public int count();

  public long longCount();

  public Class<?> getElementType();

  public Expression<?> getExpression();

  public IQueryProvider getProvider();




  /**
   * Project using a different type
   * @param clazz
   * @return
   */
  public <TResult> IRavenQueryable<TResult> as(Class<TResult> clazz);


  //TODO: finish me

}
