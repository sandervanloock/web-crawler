interface Page {
  page: number;
  pageSize: number;
  pageCount: number;
  total: number;
}

export interface StrapiPageWrapper<T> {
  data: StrapObjectWrapper<T>[];
  meta: {
    pagination: Page;
  }
}

export interface StrapObjectWrapper<T> {
  id: number;
  attributes: T;
}