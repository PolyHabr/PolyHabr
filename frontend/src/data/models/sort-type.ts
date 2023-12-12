export namespace Sort {
  export class Type {
    title: String = "";
    hasOptions: Boolean = false;
    selectedOption: Option | undefined = undefined;

    constructor(title: String, hasOptions: Boolean = false) {
      this.title = title;
      this.hasOptions = hasOptions;
    }

    selectOption(option: Option | undefined) {
      this.selectedOption = option;
    }

    clearOption() {
      this.selectOption(undefined);
    }
  }

  export class Option {
    title: String = "";
    data: String | null = "";

    constructor(title: String, data: String | null) {
      this.title = title;
      this.data = data;
    }
  }
}
