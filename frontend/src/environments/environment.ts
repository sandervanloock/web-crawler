// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  cms: {
    host: 'http://localhost:10337',
    token: 'f1d07c7fdd2075b2ddb61bcd346860219107bfe9e3352882fe66c3425a26ca7fe643fc4cdeaf250cd8920388d7ea1fd48cd5dafae5f7ca683f5291fc503de66a90d4ee1c4f77f4f01f0ce2e68dda98087914e87a5e7a1fd04c3d858bd210b94483eac6976f4d687cf95fd1d0f3a52bf9700a477242c67b248c550f0e03de4d93'
  },
  crawler: {
    host: 'http://localhost:8082'
  },
  consumer: {
    host: 'http://localhost:8081'
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
