open System

let minimumDistance array =
    let result = 
        array 
        |> List.mapi (fun i x -> (x, i))
        |> List.groupBy (fun (x, i) -> x)
        |> List.filter (fun (_, i) -> i.Length > 1)
        |> List.map (fun (x, tuples) -> tuples 
                                        |> List.map (fun (_, i) -> i) 
                                        |> List.reduce (fun acc i -> i - acc))
    
    if result |> List.isEmpty then -1 else result |> List.min

[<EntryPoint>]
let main argv = 
    let readLine = Console.ReadLine
    let toInt = System.Int32.Parse
    let readSpaceSeparatedInts() = 
        readLine().Split(' ')
        |> Array.map (fun x -> toInt(x)) |> Array.toList

    let n = readLine() |> toInt
    let array = readSpaceSeparatedInts()
    
    array |> minimumDistance |> printfn "%A"

    0 // return an integer exit code
