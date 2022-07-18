Design Notes
=============

Universally Unique Mappings
****************************

The universally-unique-mapping entity is a simple persistent key-value entity that is used for many things. It
was initially created to save report parameters dynamically without changing the structure of reports.
Later though it was then used to configure global values, especially because the universal key is required
and unique.

Once setup the client API can look for a value by using the internal-universally-unique-mapping-service
to get the value with a single string key argument.
The implementation is a simple repository that runs a findByKey query and returns the value.
We could for instance use the key to store a value like ``globallyPreferredIso4217CurrencyCode`` and seek that
value whenever we are about to create a settlement entity, or some "preferred department...

    ```
        updatePreferredDepartment(): void {
            // TODO Replace with entity filters
            this.universallyUniqueMappingService.search({ page: 0, size: 0, sort: [], query: "globallyPreferredReportDesignDepartmentDealer"})
              .subscribe(({ body }) => {
                if (body!.length > 0) {
                  if (body) {
                    this.dealerService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: body[0].mappedValue })
                      .subscribe(({ body: dealers }) => {
                        if (dealers) {
                          this.editForm.get(['department'])?.setValue(dealers[0]);
                        }
                      });
                  }
                }
              });
        }
    ```
