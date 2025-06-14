import { Button } from "@/components/ui/button";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSeparator,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import { REGEXP_ONLY_DIGITS_AND_CHARS } from "input-otp";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form, FormField, FormItem, FormControl } from "@/components/ui/form";
import React from "react";

const inviteCodeSchema = z.object({
  code: z.string().length(8, "Code must be 8 characters"),
});

const InviteCodeForm = ({ onSubmit }: { onSubmit: (code: string) => void }) => {
  const form = useForm({
    resolver: zodResolver(inviteCodeSchema),
    defaultValues: { code: "" },
    mode: "onChange",
  });
  const code = form.watch("code");

  React.useEffect(() => {
    if (code.length === 8 && form.formState.isValid) {
      form.handleSubmit((values) => onSubmit(values.code))();
    }
    // eslint-disable-next-line
  }, [code, form.formState.isValid]);

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit((values) => onSubmit(values.code))}
        className="space-y-4"
      >
        <FormField
          control={form.control}
          name="code"
          render={({ field }) => (
            <FormItem>
              <div className="mt-4 text-2xl font-semibold text-muted-foreground">
                Enter your invitation code:
              </div>
              <FormControl>
                <InputOTP
                  maxLength={8}
                  pattern={REGEXP_ONLY_DIGITS_AND_CHARS}
                  value={field.value}
                  onChange={(val) => field.onChange(val.toUpperCase())}
                >
                  <InputOTPGroup>
                    <InputOTPSlot index={0} />
                    <InputOTPSlot index={1} />
                    <InputOTPSlot index={2} />
                    <InputOTPSlot index={3} />
                  </InputOTPGroup>
                  <InputOTPSeparator />
                  <InputOTPGroup>
                    <InputOTPSlot index={4} />
                    <InputOTPSlot index={5} />
                    <InputOTPSlot index={6} />
                    <InputOTPSlot index={7} />
                  </InputOTPGroup>
                </InputOTP>
              </FormControl>
            </FormItem>
          )}
        />
        <Button
          type="submit"
          disabled={!form.formState.isValid}
          className="w-full sm:w-auto"
        >
          Submit
        </Button>
      </form>
    </Form>
  );
};

export default InviteCodeForm;
