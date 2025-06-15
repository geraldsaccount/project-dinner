import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import React from "react";

interface FormTextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  containerClassName?: string;
}

const FormTextarea: React.FC<FormTextareaProps> = ({
  label,
  value,
  onChange,
  containerClassName = "",
  ...textareaProps
}) => (
  <div className={containerClassName}>
    <Label>{label}</Label>
    <Textarea value={value} onChange={onChange} {...textareaProps} />
  </div>
);

export default FormTextarea;
