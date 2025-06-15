import { z } from "zod";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Plus } from "lucide-react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect } from "react";

export function AddPlayerConfigForm({
  minPlayers,
  maxPlayers,
  onAdd,
  existingCounts,
}: {
  minPlayers: number;
  maxPlayers: number;
  onAdd: (count: number) => void;
  existingCounts: number[];
}) {
  const effectiveMinPlayers = Math.max(1, minPlayers);
  const playerConfigSchema = (min: number, max: number) =>
    z.object({
      playerCount: z
        .string()
        .min(1, "Required")
        .refine(
          (val) => {
            const num = parseInt(val, 10);
            return !isNaN(num) && num >= min && num <= max;
          },
          {
            message: `Player count must be between ${min} and ${max}`,
          }
        ),
    });

  const form = useForm({
    resolver: zodResolver(playerConfigSchema(effectiveMinPlayers, maxPlayers)),
    defaultValues: { playerCount: "" },
  });

  useEffect(() => {
    form.reset({ playerCount: "" });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [minPlayers, maxPlayers]);

  const playerCount = form.watch("playerCount");
  const num = parseInt(playerCount, 10);
  const isDuplicate = existingCounts.includes(num);
  const isDisabled =
    !playerCount ||
    isNaN(num) ||
    num < effectiveMinPlayers ||
    num > maxPlayers ||
    isDuplicate;

  return (
    <Card>
      <CardHeader>
        <CardTitle>Add New Player Configuration</CardTitle>
        <CardDescription>
          Define which characters are used for a specific number of players.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form
            onSubmit={form.handleSubmit((data) => {
              onAdd(parseInt(data.playerCount, 10));
              form.reset();
            })}
            className="flex items-center gap-4"
          >
            <FormField
              control={form.control}
              name="playerCount"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Player Count</FormLabel>
                  <FormControl>
                    <div className="flex gap-2">
                      <Input
                        type="number"
                        inputMode="numeric"
                        pattern="[0-9]*"
                        min={effectiveMinPlayers}
                        max={maxPlayers}
                        placeholder={`e.g., ${effectiveMinPlayers + 1}`}
                        {...field}
                        onBeforeInput={(e) => {
                          if (!/\d/.test(e.data ?? "")) {
                            e.preventDefault();
                          }
                        }}
                      />
                      <Button type="submit" disabled={isDisabled}>
                        <Plus />
                        Add Configuration
                      </Button>
                    </div>
                  </FormControl>
                  <FormMessage />
                  {isDuplicate && (
                    <div className="text-xs text-red-500 mt-1">
                      A configuration for {num} players already exists.
                    </div>
                  )}
                </FormItem>
              )}
            />
          </form>
        </Form>
      </CardContent>
    </Card>
  );
}
