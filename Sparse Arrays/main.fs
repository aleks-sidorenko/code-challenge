open System

[<EntryPoint>]
let main argv = 
    let read = Console.ReadLine
    let toInt = System.Int32.Parse
    let n = read() |> toInt
    let values = [ for i in 1..n do yield read() ]

    let q = read() |> toInt
    let queries = [ for i in 1..q do yield read() ]

    let values =
        values
        |> List.groupBy (fun x -> x)
        |> List.map (fun (key, values) -> (key, values.Length))
        |> Map.ofList
    
    let result =
        queries
        |> List.map (fun x -> if values.ContainsKey x then values.[x] else 0)
        |> List.iter (fun x -> printfn "%A" x)

    0 // return an integer exit code
