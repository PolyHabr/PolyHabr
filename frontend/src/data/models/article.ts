export namespace Article {
  export class Item {
    id: number = 0;
    title: string = "";
    user: User = new User("");
    listTag: string[] = [];
    listDisciplineName: string[] = [];
    typeId: Tag = new Tag("Test");
    date: string = "";
    previewText: string = "";
    previewImgId: string = "";
    text: string = "";
    fileId: number = 0;
    likes: number = 0;
    viewCount: number = 0;
    isSaveToFavourite: boolean = false;

    filePdf: string = "";


    constructor(title: string, listTag: string[], listDisciplineName: string[], date: string, previewText: string, fileId: number, previewId: string) {
      this.title = title;
      this.listTag = listTag;
      this.listDisciplineName = listDisciplineName;
      this.date = date;
      this.previewText = previewText;
      this.fileId = fileId;
      this.previewImgId = previewId;
    }

    static createTemporary(): Article.Item {
      return new Article.Item("REST API в микросервисной архитектуре", [
        "Блог компании Издательский дом «Питер»",
        "API",
        "Микросервисы",
        "Проектирование и рефакторинг",
      ], ["Test"], "сегодня 12:42", "<a href=\"https://habr.com/ru/company/piter/blog/698798/\">\n" +
        "      <img src=\"https://habrastorage.org/webt/yd/vg/_l/ydvg_lgfrb1nekjmmza0qmbvmoy.jpeg\"\n" +
        "           alt=\"image\">\n" +
        "    </a>\n" +
        "    <br>\n" +
        "    В этом посте расскажу о том, какой вред может нанести межсервисная коммуникация по HTTP в микросервисной архитектуре\n" +
        "    и предложу альтернативный способ совместного использования данных в распределенной системе.", 1, "1")
    }
  }

  export class Tag {
    id: number = 0;
    name: string = "";

    constructor(title: string) {
      this.name = title;
    }
  }

  export class Type {
    name: string = "";

    constructor(title: string) {
      this.name = title;
    }
  }

  export class User {
    id: number = 0;
    name: string = "Student";
    surname: string = "Student";
    login: string = "";
    email: string = "";

    constructor(nickname: string) {
      this.login = nickname;
    }
  }

  export class Comment {
    userId: User = new User("User");
    text: string = "";
    date: string = "";

    constructor(text: string) {
      this.text = text;
    }
  }
}
