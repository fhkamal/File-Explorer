import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import java.io.FileInputStream
import javafx.scene.image.ImageView
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import java.nio.file.FileSystems
import java.util.*

class Main : Application()  {
    override fun start(stage: Stage) {

        var dir = "${System.getProperty("user.dir")}/test/"
        val top = dir
        var hidden = true
        val dia = Dialog<String>()
        dia.title = "Dialog"

        // create the root of the scene graph
        // BorderPane supports placing children in regions around the screen
        val layout = BorderPane()

        // top: menubar
        val menuBar = MenuBar()
        val fileMenu = Menu("File")
        val fileNew = MenuItem("New")
        val fileOpen = MenuItem("Open")
        val fileBack = MenuItem("Back")
        val fileHome = MenuItem("Home")
        val viewMenu = Menu("View")
        val actionMenu = Menu("Actions")
        val actionRename = MenuItem("Rename")
        val actionMove = MenuItem("Move")
        val actionDelete = MenuItem("Delete")
        val optionMenu = Menu("Options")
        val optionHidden = MenuItem("Show Hidden Files")

        menuBar.menus.add(fileMenu)
        fileMenu.items.add(fileNew)
        fileMenu.items.add(fileOpen)
        fileMenu.items.add(fileBack)
        fileMenu.items.add(fileHome)
        menuBar.menus.add(viewMenu)
        menuBar.menus.add(actionMenu)
        actionMenu.items.add(actionRename)
        actionMenu.items.add(actionMove)
        actionMenu.items.add(actionDelete)
        menuBar.menus.add(optionMenu)
        optionMenu.items.add(optionHidden)

        val buttonBar = ToolBar()
        val homeButton = Button("Home")
        val prevButton = Button("Prev")
        val nextButton = Button("Next")
        val renameButton = Button("Rename")
        val moveButton = Button("Move")
        val deleteButton = Button("Delete")
        buttonBar.items.add(homeButton)
        buttonBar.items.add(prevButton)
        buttonBar.items.add(nextButton)
        buttonBar.items.add(renameButton)
        buttonBar.items.add(moveButton)
        buttonBar.items.add(deleteButton)

        val topScreen = VBox()
        topScreen.children.add(menuBar)
        topScreen.children.add(buttonBar)

        // handle default user action aka press
        fileNew.setOnAction {
            println("New pressed")
        }

        fileNew.accelerator = KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)

        // left: tree
        var tree = ListView<File>()
        var copy = ListView<String>()
        File(dir).listFiles().forEach {
            var dot = true
            if (it.name[0] == '.' && hidden){
                dot = false
            }
            if (dot) {
                tree.items.add(it)
                var n = it.name
                if (it.isDirectory){
                    n += '/'
                }
                copy.items.add(n)
            }
        }

        var c = 0

        // text in center
        var display = VBox(TextArea())
        VBox.setVgrow(display.children[0], Priority.ALWAYS)
        if (tree.items[c].isFile){
            if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                display.children.set(0, ImageView(image))
            }
            else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                val t = TextArea(tree.items[c].readText())
                t.isWrapText = true
                display.children.set(0, t)
                VBox.setVgrow(display.children[0], Priority.ALWAYS)
            }
        }
        else{
            display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
        }


        //bottom text
        val status = Label(tree.items[c].path)
        status.focusTraversableProperty().set(false)

        fileOpen.setOnAction {
            if (tree.items[c].isDirectory){
                dir = dir + copy.items[c]
                println(dir)
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true){
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory){
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
        }

        fileBack.setOnAction {
            if (File(dir) != File(top)){
                dir = File(dir).parent + "/"
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true){
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory){
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
        }

        fileHome.setOnAction {
            dir = top
            var tree2 = ListView<File>()
            var copy2 = ListView<String>()
            File(dir).listFiles().forEach {
                var dot = true
                if (it.name[0] == '.' && hidden == true){
                    dot = false
                }
                if (dot) {
                    tree2.items.add(it)
                    var n = it.name
                    if (it.isDirectory){
                        n += '/'
                    }
                    copy2.items.add(n)
                }
            }

            c = 0
            tree.setItems(tree2.items)
            copy.setItems(copy2.items)
            status.setText(tree.items[c].path)
            if (tree.items[c].isFile){
                if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                    println(tree.items[c].extension)
                    var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                    display.children.set(0, ImageView(image))
                }
                else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                    val t = TextArea(tree.items[c].readText())
                    t.isWrapText = true
                    display.children.set(0, t)
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
            else{
                display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
            }
        }

        actionRename.setOnAction {
            val choose = FileChooser()
            choose.initialDirectory = File(dir)
            choose.title = "Rename File"
            val dest = choose.showSaveDialog(stage)
            if (dest == null){
                val al = Alert(Alert.AlertType.ERROR, "Rename failed", ButtonType.OK)
                al.showAndWait()
            }
            else {
                println(dest.name)
                tree.items[c].renameTo(dest)
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true) {
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory) {
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }

        }

        actionMove.setOnAction {
            val choose = FileChooser()
            choose.initialDirectory = File(dir)
            choose.title = "Move File"
            val dest = choose.showSaveDialog(stage)
            if (dest == null){
                val al = Alert(Alert.AlertType.ERROR, "Move failed", ButtonType.OK)
                al.showAndWait()
            }
            else {
                println(dest.name)
                tree.items[c].renameTo(dest)
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true) {
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory) {
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }

        }

        actionDelete.setOnAction {
            val al = Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete", ButtonType.YES, ButtonType.CANCEL)
            al.showAndWait()

            if (al.result == ButtonType.YES) {
                if (tree.items[c].isFile) {
                    File(dir + copy.items[c]).delete()
                } else {
                    File(dir + copy.items[c]).deleteRecursively()
                }
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true) {
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory) {
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
        }

        optionHidden.setOnAction {
            hidden = !hidden
            var tree2 = ListView<File>()
            var copy2 = ListView<String>()
            File(dir).listFiles().forEach {
                var dot = true
                if (it.name[0] == '.' && hidden == true){
                    dot = false
                }
                if (dot) {
                    tree2.items.add(it)
                    var n = it.name
                    if (it.isDirectory){
                        n += '/'
                    }
                    copy2.items.add(n)
                }
            }

            c = 0
            tree.setItems(tree2.items)
            copy.setItems(copy2.items)
            status.setText(tree.items[c].path)
            if (tree.items[c].isFile){
                if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                    println(tree.items[c].extension)
                    var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                    display.children.set(0, ImageView(image))
                }
                else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                    val t = TextArea(tree.items[c].readText())
                    t.isWrapText = true
                    display.children.set(0, t)
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
            else{
                display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
            }
        }

        homeButton.setOnAction {
            dir = top
            var tree2 = ListView<File>()
            var copy2 = ListView<String>()
            File(dir).listFiles().forEach {
                var dot = true
                if (it.name[0] == '.' && hidden == true){
                    dot = false
                }
                if (dot) {
                    tree2.items.add(it)
                    var n = it.name
                    if (it.isDirectory){
                        n += '/'
                    }
                    copy2.items.add(n)
                }
            }

            c = 0
            tree.setItems(tree2.items)
            copy.setItems(copy2.items)
            status.setText(tree.items[c].path)
            if (tree.items[c].isFile){
                if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                    println(tree.items[c].extension)
                    var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                    display.children.set(0, ImageView(image))
                }
                else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                    val t = TextArea(tree.items[c].readText())
                    t.isWrapText = true
                    display.children.set(0, t)
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
            else{
                display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
            }
        }

        prevButton.setOnAction {
            if (File(dir) != File(top)){
                dir = File(dir).parent + "/"
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true){
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory){
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
        }

        nextButton.setOnAction {
            if (tree.items[c].isDirectory){
                dir = dir + copy.items[c]
                println(dir)
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true){
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory){
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
        }

        renameButton.setOnAction {
            val choose = FileChooser()
            choose.initialDirectory = File(dir)
            choose.title = "Rename File"
            val dest = choose.showSaveDialog(stage)
            if (dest == null){
                val al = Alert(Alert.AlertType.ERROR, "Rename failed", ButtonType.OK)
                al.showAndWait()
            }
            else {
                println(dest.name)
                tree.items[c].renameTo(dest)
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true) {
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory) {
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }

        }

        moveButton.setOnAction {
            val choose = FileChooser()
            choose.initialDirectory = File(dir)
            choose.title = "Move File"
            val dest = choose.showSaveDialog(stage)
            if (dest == null){
                val al = Alert(Alert.AlertType.ERROR, "Move failed", ButtonType.OK)
                al.showAndWait()
            }
            else {
                println(dest.name)
                tree.items[c].renameTo(dest)
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true) {
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory) {
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }

        }

        deleteButton.setOnAction {
            val al = Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete", ButtonType.YES, ButtonType.CANCEL)
            al.showAndWait()

            if (al.result == ButtonType.YES) {
                if (tree.items[c].isFile) {
                    File(dir + copy.items[c]).delete()
                } else {
                    File(dir + copy.items[c]).deleteRecursively()
                }
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true) {
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory) {
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
            }
        }

        // handle mouse clicked action
        copy.setOnMouseClicked { event ->
            if (event.clickCount == 1) {
                c = copy.items.indexOf(copy.getSelectionModel().getSelectedItem())
                println(event)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }

            }
            else if (event.clickCount == 2 && tree.items[c].isDirectory){
                dir = dir + copy.items[c]
                println(dir)
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true){
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory){
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
        }
        copy.setOnKeyPressed { event ->
            println(event.code)
            if (event.code == KeyCode.ENTER && tree.items[c].isDirectory){
                dir = dir + copy.items[c]
                println(dir)
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true){
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory){
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
            else if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE && File(dir) != File(top)){
                dir = File(dir).parent + "/"
                var tree2 = ListView<File>()
                var copy2 = ListView<String>()
                File(dir).listFiles().forEach {
                    var dot = true
                    if (it.name[0] == '.' && hidden == true){
                        dot = false
                    }
                    if (dot) {
                        tree2.items.add(it)
                        var n = it.name
                        if (it.isDirectory){
                            n += '/'
                        }
                        copy2.items.add(n)
                    }
                }

                c = 0
                tree.setItems(tree2.items)
                copy.setItems(copy2.items)
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }

            }
            else if (event.code == KeyCode.DOWN && c!=tree.items.size-1){
                c++
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        println(tree.items[c].extension)
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
            else if (event.code == KeyCode.UP && c!=0){
                c--
                status.setText(tree.items[c].path)
                if (tree.items[c].isFile){
                    if(tree.items[c].extension == "jpg" || tree.items[c].extension == "png" || tree.items[c].extension == "bmp"){
                        var image = Image(FileInputStream(tree.items[c]), 400.00, 400.00, false, false)
                        display.children.set(0, ImageView(image))
                    }
                    else if(tree.items[c].extension == "md" || tree.items[c].extension == "txt"){
                        val t = TextArea(tree.items[c].readText())
                        t.isWrapText = true
                        display.children.set(0, t)
                        VBox.setVgrow(display.children[0], Priority.ALWAYS)
                    }
                }
                else{
                    display.children.set(0, TextArea())
                    VBox.setVgrow(display.children[0], Priority.ALWAYS)
                }
            }
        }


        // build the scene graph
        layout.top = topScreen
        layout.left = copy
        layout.center = display
        layout.bottom = status

        // create and show the scene
        val scene = Scene(layout)
        stage.width = 800.0
        stage.height = 500.0
        stage.scene = scene
        stage.show()
    }
}