export abstract class ApiResult<T> {
  abstract contents: T;
  totalElements: number = 0;
  totalPages: number = 0;
}
