<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <meta charset="UTF-8">
    <title>Add Server</title>

    <script>
        $(document).ready(function () {
            $('#addServer').submit(function () {
                $.ajax({type: 'POST', url: '/register.php', data: $(this).serialize()});
                return false;
            });
        });
    </script>
    <noscript>Sorry, your browser does not support JavaScript!</noscript>
</head>
<body>
<div id="server">
    <form id="addServer" method="POST">
        <label>
            Name:
            <input type="text" name="name">
        </label>
        <label>
            <br>
            Password:
            <input type="text" name="password">
        </label>
        <label>
            <br>
            Server Type:
            <select name="serverType">
                <option>MINECRAFT</option>
                <option>ARK</option>
                <option>CSGO</option>
                <option>CSS</option>
                <option>AVORION</option>
                <option>ARMA3</option>
                <option>FACTORIO</option>
            </select>
        </label>
        <br>
        <input type="submit" name="registerServer" id="registerServer" value="Register">
    </form>
</div>

</body>
</html>