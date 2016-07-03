open System

[<EntryPoint>]
let main argv = 
    let readLine = Console.ReadLine
    let toInt = System.Int32.Parse
    let readSpaceSeparatedInts() = 
        readLine().Split(' ')
        |> Array.map (fun x -> toInt(x))

    let t = readLine() |> toInt
    for i in 1..t do
        let line = readSpaceSeparatedInts()
        let n = line.[0]
        let k = line.[1]
        let students = readSpaceSeparatedInts()
        let present = students |> Array.filter (fun x -> x <= 0) |> Array.length
        let cancel = if k <= present then "NO" else "YES"
        printfn "%s" cancel


    0 // return an integer exit code
